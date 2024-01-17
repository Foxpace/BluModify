# BluModify

<p align="center">
<img src="https://github.com/Foxpace/BluModify/blob/master/blumodify_icon.png" width="250">
</p>
<p align="center">
<img src="https://github.com/Foxpace/BluModify/blob/master/blumodify_app.jpg" width="250">
</p>

BluModify is a simple Android app that will warn you about redundant Bluetooth activity. If you leave your Bluetooth on, the app will warn you about this situation and you can turn it off. 
Unfortunately, the programmatic way of turning off Bluetooth was removed with higher versions of Android, so the app aligns with this decision. 

# Motivation and main purpose

Active Bluetooth presents a couple of problems of which character, not many people are aware:
* Some shops are tracking customers by looking for active Bluetooth devices - anyone can do it in the public
* A more serious issue is that attackers can make phishing attacks, steal private data or eavesdrop on your conversations
* Bluetooth is a battery eater - even though with advances in Bluetooth technology, it still consumes a significant portion of the battery

**Only by turning off the Bluetooth, you can solve all of the issues above.**

# Technical perspective

## Some highlights of implementation
* UI is done purely with Jetpack Compose
* MVI architecture
* Dependency injection via Hilt - UI is fully decoupled from ViewModels and repositories
* WorkManager implementation
* Logging and error handling for all the cases - transparent logs in the settings of the app 
* All the repositories and ViewModels are covered by tests (if it is possible)
* Walk UI tests for all of the screens with basic user use cases

## Known issues
If you run the app on a phone that has tampered with battery savings, the app will stop working after a certain amount of time because the scheduled worker is removed from the WorkManager. Check this site for more information: https://dontkillmyapp.com/
