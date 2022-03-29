# AndroidFirebaseLearning

A simple app made to learn Firebase along with local data storage for Android

## Table of Contents

- [Description](#description)

## Description

This is a simple Android log-in/log-out/register app, made to learn:
- Local data storage using `SharedPreferences` for primitives. This is used to store short messages to be shared between activities such as Toast messages.
- Local data storage using the `Room` persistence library for structured data using SQLite. This is used to store all the user's data locally for authentication...which probably won't be very secure, so a future implementation should probably conduct authentication and store only the authenticated user's data.
- Cloud data storage using Firebase Realtime Database. 
