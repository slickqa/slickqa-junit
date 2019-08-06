# slickqa-junit
Slick Junit connector

To post results to Slick you will need to provide some Java opts.
-Dslick.baseurl=<Slick Instance Base URL>
-Dslick.project=<project to post to>
-Dslick.release=<release to post to>
-Dslick.build=<build to post to>

For example:
-Dslick.baseurl=http://slick.mycompany.com -Dslick.project=automation -Dslick.release=junit4 -Dslick.build=86-4pm-suite
