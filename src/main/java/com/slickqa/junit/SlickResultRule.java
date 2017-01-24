package com.slickqa.junit;

import com.slickqa.client.SlickClient;
import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.Result;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * JUnit rule to use inside the tests
 */
public class SlickResultRule extends TestWatcher {

    private SlickJunitController slickJunitController;

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
                update.setStatus("FAIL");
                update.setRunstatus("SKIPPED");
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
        if(isUsingSlick()) {
            Result result = getSlickJunitController().getOrCreateResultFor(description);
            Result update = new Result();
            update.setStarted(new Date());
            update.setReason("");
            update.setRunstatus("RUNNING");
            try {
                result = getSlickClient().result(result.getId()).update(update);
            } catch (SlickError e) {
                e.printStackTrace();
                System.err.println("!! ERROR: Unable to set result to starting. !!");
            }
        }
    }
}
