ARApp
=====

A project for my paper, still under development.


Author: Dandan Meng

I'm a student in BIT,Beijing,China.
email: mengdandanno1@163.com
Please contract me if you have interest in the project.
since: 2013-07-01


2013-07-10 Basic compass finished.

The basic compass:
No matter the device are hold vertical or parallel to the ground, the direction infomation shown on the compass is the same.
This is thankful to the remapCoordinates method in CompassViewModel. 
The Arrow are shown in different way to distinguish the vertical and parallel compass.

2013-07-15 Current Location information gained.

The basic location part:
The choice of location provider: GPS, network, passive.
The location source settings intent.
The request of update and unregister through LocationManager.
There is a UI for the user to control, still thinking about one without UI.

2013-07-22 Basic POI and Radar graph, and Baidu Map

The POI information is gained through network: Google Place and Wiki.
The Radar is adapter from Justin Wetherell's work: android-augment-reality-framework.
Baidu Map and location SDK are also added.
reference links:
http://code.google.com/p/android-augment-reality-framework/
Google Place API:
https://developers.google.com/places/documentation/

2013-10-08 long time no commit.

Due to personal reasons, the project is developing at a strange pace.
A lot work has been done during the past national holiday.
Such as the keywords navigation, the updates of Baidu Map SDK and location SDK, the direction data in the SensorViewModel.
I give up the computation in android-augment-reality-framework, and use a new one(which I picked up and turned out to work) to get the direction.
I also use the angle to place the AR Markers and handled the collisions of markers in a pretty simple way.
Also, I began to use a new formatter which put the left brace in the end of line.
I think this may be the main reason for so much updates. Haha~

2013-12-02 AutoNavi SDK added
Since AutoNavi's SDK is quite similar to Baidu Map SDK, it's easy to add AutoNavi's Map and Location SDK to the framework.
The same basic classes were used.



