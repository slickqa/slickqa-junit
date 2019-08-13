# slickqa-junit
Slick Junit connector

To post results to Slick you will need to provide some Java opts.

-Dslick.baseurl=&lt;Slick Instance Base URL&gt;

-Dslick.project=&lt;project to post to&gt;

-Dslick.release=&lt;release to post to&gt;

-Dslick.build=&lt;build to post to&gt;

For example:
-Dslick.baseurl=http://slick.mycompany.com -Dslick.project=automation -Dslick.release=junit4 -Dslick.build=86-4pm-suite
