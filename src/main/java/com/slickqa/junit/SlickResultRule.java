package com.slickqa.junit;

import com.slickqa.client.SlickClient;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.Result;
import com.slickqa.client.model.StoredFile;
import com.slickqa.junit.annotations.SlickMetaData;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JUnit rule to use inside the tests
 */
public class SlickResultRule extends TestWatcher {

    private SlickJunitController slickJunitController;

    private ThreadLocal<Result> currentResult;

    private boolean triedToInitialize;

    private SlickClient getSlickClient() {
        if(isUsingSlick()) {
            return getSlickJunitController().getSlickClient();
        } else {
            return null;
        }
    }

    public boolean isUsingSlick() {
        boolean retval = false;

        SlickJunitController controller = getSlickJunitController();
        if(controller != null && controller.isUsingSlick()) {
            retval = true;
        }

        return retval;
    }

    private SlickJunitController getSlickJunitController() {
        if(!triedToInitialize) {
            slickJunitController = SlickJunitControllerFactory.getControllerInstance();
            triedToInitialize = true;
        }
        return slickJunitController;
    }

    public SlickResultRule() {
        triedToInitialize = false;
        slickJunitController = null;
        currentResult = new ThreadLocal<>();
        currentResult.set(null);
    }

    private void addFileToResult(String resultId, StoredFile file) {
        try {
            Result current = getSlickClient().result(resultId).get();
            List<StoredFile> files = current.getFiles();
            if(files == null) {
                files = new ArrayList<>(1);
            }
            files.add(file);
            Result update = new Result();
            update.setFiles(files);
            getSlickClient().result(current.getId()).update(update);
        } catch (SlickError e) {
            e.printStackTrace();
            System.err.println("!! ERROR: adding file to result " + resultId + " !!");
        }

    }

    public void addFile(Path localPath) {
        if(isUsingSlick()) {
            Result current = currentResult.get();
            if(current != null) {
                StoredFile file = null;
                try {
                    file = getSlickClient().files().createAndUpload(localPath);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!! ERROR: unable to upload file " + localPath.toString() + " !!");
                }
                if (file != null) {
                    addFileToResult(current.getId(), file);
                }
            } else {
                System.err.println("!! WARNING: no current result when trying to add " + localPath.toString() + " !!");
            }
        }
    }

    public void addFile(String filename, String mimetype, InputStream inputStream) {
        if(isUsingSlick()) {
            Result current = currentResult.get();
            if(current != null) {
                StoredFile file = null;
                try {
                    file = getSlickClient().files().createAndUpload(filename, mimetype, inputStream);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!! ERROR: unable to upload file " + filename + " !!");
                }
                if (file != null) {
                    addFileToResult(current.getId(), file);
                }
            } else {
                System.err.println("!! WARNING: no current result when trying to add " + filename + " !!");
            }
        }
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
        if(isUsingSlick()) {
            Result result = getSlickJunitController().getResultFor(description);
            if(result != null) {
                Result update = new Result();
                update.setFinished(new Date());
                update.setStatus("PASS");
                update.setRunstatus("FINISHED");
                try {
                    getSlickClient().result(result.getId()).update(update);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!! ERROR: Unable to pass result !!");
                }
            }
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        if(isUsingSlick()) {
            Result result = getSlickJunitController().getResultFor(description);
            if(result != null) {
                Result update = new Result();
                update.setFinished(new Date());
                update.setStatus("FAIL");
                update.setRunstatus("FINISHED");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                update.setReason(e.getMessage() + "\n" + sw.toString());
                try {
                    getSlickClient().result(result.getId()).update(update);
                } catch (SlickError err) {
                    err.printStackTrace();
                    System.err.println("!! ERROR: Unable to pass result !!");
                }
            }
        }
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
        super.skipped(e, description);
        if(isUsingSlick()) {
            Result result = getSlickJunitController().getResultFor(description);
            if(result != null) {
                Result update = new Result();
                update.setFinished(new Date());
                update.setStatus("SKIPPED");
                update.setRunstatus("FINISHED");
                update.setReason(e.getMessage());
                try {
                    getSlickClient().result(result.getId()).update(update);
                } catch (SlickError err) {
                    err.printStackTrace();
                    System.err.println("!! ERROR: Unable to pass result !!");
                }
            }
        }
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);
        if(isUsingSlick() && description.getAnnotation(SlickMetaData.class) != null) {
            Result result = getSlickJunitController().getOrCreateResultFor(description);
            Result update = new Result();
            update.setStarted(new Date());
            update.setReason("");
            update.setRunstatus("RUNNING");
            try {
                result = getSlickClient().result(result.getId()).update(update);
                currentResult.set(getSlickClient().result(result.getId()).get());
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!! ERROR: Unable to set result to starting. !!");
                currentResult.set(null);
            }
        } else {
            currentResult.set(null);
        }
    }
}
