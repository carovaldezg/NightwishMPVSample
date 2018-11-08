# NightwishMVPSample
This sample covers the following topics: MVP with Intent states architecture, Portrait and landscape layout support, field validations, avoid multiple click events, rotation support to restore input data on device rotation, adding one attachment from the gallery, runtime gallery permission, drawables for rounded style buttons, styles-colors and dimens files. The code has been implemented following high quality code practices.

## Download it now from Google Playstore! https://play.google.com/store/apps/details?id=caro.helpdeskapp

# MVP

If you are not familiar with MVP, then this is a perfect short sample to start with! MVP is a well known pattern used for mobile development. All you need to know is this:

M = Model, it is a container class. It means that it only has attributes, getters and setters. In this sample, as I couldn't find a server where to send this data, I don't have a FormModel, but i will continue working on this. It will have the strings related to the form fields and the image.

V = View, it is a contract, a promise of the implementation of a method definition that somebody will implement. For MVP, that somebody is the Presenter. Through this view, the Presenter manages the screen changes.

P = Presenter, it is in charge of managing all the logic of a view.

Shortly, the view (layout, screen or visual UI) is dumb, it only shows or hides elements on the screen. The view follows what the presenter commands. The presenter is the one who knows what to do, takes care of that and tells the view "hey babe, execute this method".

The presenter has a reference to the screen (Activity or Fragment) and calls the different methods that this one has to perform through the View. Those methods sometimes includes information, that's where the models are used.

The curius element in this sample is that I am not using a regular MVP, but an MVI, "I" from Intent. For a better readability and quality code, I use States classes inside the interfaces. every state defines a behavior for the activity. Thanks to this, your activity goes from one state to other, asking what's the instance of the state that is being requested from the presenter. All this management of states is performed in only one method: render. 

This is the flow: The Activity ask the presenter to take a decision, the presenter processes the information and tells the activity to make changes by rendering a new state on the screen. The activity has a method called "render" that asks what kind of state the presenter has sent it and calls the method that will show the changes in the screen.


# Responsive Layout (landscape - portrait)

As you can see, this sample has 2 layouts, a form and a confirmation screen. Both layouts have been implemented 2 times: one for landscape mode and one for portrait mode. How do you manage this? Well, it's super simple, just create a new folder called "layout-land" and inside that folder you will create the layout files for the landscape mode. Android manages to understand this, so you only have to worry about how do you want your layour to look.

In this sample, I have used different pictures, so you can see the clear difference between the layouts when you rotate your device.

For portrait:

![Alt text](https://github.com/carovaldezg/NightwishMPVSample/blob/master/Screenshot_1541016858.png)

For landscape: 

![Alt text](https://github.com/carovaldezg/NightwishMPVSample/blob/master/Screenshot_1541016845.png)

**Be aware of naming all the elements with the same ID! otherwise your app will crash**

# Field validations

There are 3 mandatory fields in the form: name, email and phone number. None of them can be empty, additionally, phone and email need to have a specific format, which is controlled by the "Pattern" class provided by Java.
So, everytime the Activity receives an input, it will call the Presenter to notify it of these changes. The presenter saves the values of the inputs and checks if all the mandatory fields have been completed. If so, it will tell the Activity to enable the "send" button, which will be clieckeable and change from grey color to blue color, notifying the user that now the button can be clicked.

# Avoid multiple clicks

This is a common error that even the biggest and most known apps use to have. If you are fast enough, you can perform a double click in a clickeable element. To avoid triggering an action multiple times, it is better to disable the button after the first click. In case of error states, you should give the user the chance to re-click the button. Because of this, it is better to enable it in the method where the error is shown.
You may ask yourself who or why somebody will multiple click in a button. The answer is probably "only software people" but the reality is different. Sometimes, devices turn slow and the micro seconds of lack of response to the user may lead into multiple clicks when the phone goes back to its normal state. 

# Rotation support

A very interesting behavior for android apps is that whenever the user rotates the screen, the operative system destroys the view and recreates it all over again from scratch. Because of this, there are several elements that will not be displayed as before. 
The solution for not losing the data we have already input in the edit texts for name, email and phone is asking android to remember those values through overriding the method onSaveInstanceState.
There we will write a set of key-values that we want to store just in case the user rotates the device and destroys out layout.
To restore the values, you should override the method onRestoreInstanceState, and set the values through the key you gave them.

# Attachment support & real time permissions.

The app allows users to upload one file. The mechanism for this is given by the runtime permissions for external storage. 

# Rounded style buttons

This "fancy" look is provided by drawable files where you need to define the shape and the angle (radius) you want it to have on the corners.
In this case, you have the sample for the grey button and for the blue button, as my intention is only to change the color, not its shape.

# Styles, colors and dimens

A good developer is always thinking about the future. In order to provide continous maintenance to our projects, you must consider as a MUST to have the values in its right place. This avoids code repetition, our worst enemy!
Pay special attention to the styles files and how you can inherit a defined style to create a new one.

# Quality code

This project follows high standard quality code guidelines. You can learn and follow them to improve your skills as software developer. The code must be written in a special manner, where there's no trace of who wrote it and it is consistent in all the app. As an advice, i recommend you to follow the Wolox guidelines in this link: https://github.com/Wolox/tech-guides/tree/master/android

