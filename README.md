# Theater Ticket Booking App

This is an Android application developed for the university course Human-Computer Interaction (HCI). The project focuses on designing an accessible and user-friendly mobile interface for purchasing theater tickets, specifically tailored for older adults (ages 45+), who may experience difficulty using traditional ticketing platforms.


## Objective

The aim of the application is to address common usability barriers faced by older users, by incorporating design choices and features that improve accessibility and reduce cognitive load. The application was developed with consideration for both technical and social aspects of human-computer interaction.


## Target User Group

The primary user group consists of adults aged 45 and above, with limited experience or comfort using smartphones. Key considerations include:

- Difficulty reading small fonts
- Preference for voice input
- Confusion with multi-step interfaces
- Low confidence with digital transactions


## Features

-User Authentication: Secure login and signup functionalities with data persistence.
-Accessibility Options:
--Adjustable text sizes via a SeekBar.
--Optional notifications toggle.
--Speech-to-text input for enhanced usability.
-Conversational Interface:
--Chatbot capable of understanding user intents and providing appropriate responses.
--Custom-built LLM for natural language processing tasks.
-Ticket Booking Workflow:
-Selection of theatre plays, dates, and seating options.
-Booking confirmation and history tracking.
-Visitor Mode: Allows users to explore the application without registration.

## NLP Component

A lightweight natural language module was designed to simulate an intent recognition system (akin to an LLM). While it does not use pretrained models or third-party APIs, it attempts to extract meaning from user inputs and respond appropriately within the app. The component is implemented through:

- The `Request` class (`findIntent()` method)
- The `Response` class (`NLP()` method)

This module is limited in scope but demonstrates basic interaction modeling.


## Build and Run Instructions

1. Open the project in Android Studio.
2. Ensure that the Android SDK is properly configured.
3. Connect an Android device or use an emulator.
4. Build and run the application using Android Studio’s interface or `Run > Run 'app'`.
