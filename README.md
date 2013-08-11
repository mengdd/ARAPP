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


