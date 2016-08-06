# Alistin-Android
Lightweight Burmese Text to Speech library for Android (~15 MB).

Project Alistin was our effort in 2006-7 to make TTS and let J2ME apps read sms out in Myanmar language. This project include base recordings and a example player for advanced programmers who would like to start from base. An Android library with size optimized speech files will be release later. This project include female voice featured by Ma Thu Thu from City FM (around 2007) recorded in SAIL Studio.
The original project can be found <a href="https://github.com/htoomyintnaung/alistin-myanmar-tts">here</a>.

#Usage
It's very simple. Create a new Alistin Object and use speak(String paragraph) method to read the text.
<br />
```java
Alistin alistin = new Alistin(getApplicationContext());
alistin.speak("ကျေးဇူးတင်ပါတယ်");
```
Download and compile sample project for more details.
Please check the <a href="https://github.com/htoomyintnaung/Alistin-Android/blob/master/LICENSE.MD">LICENSE</a> before use.

#TODO
<ul>
<li>Read english words</li>
<li>Read double stack words</li>
</ul>
