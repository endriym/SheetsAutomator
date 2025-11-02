# SheetsAutomator

An Android app that leverages **Jetpack Compose** and the **Google Sheets API**s to insert data into a Google Sheet. The app uses **OAuth 2.0** for authentication.

https://github.com/user-attachments/assets/c82cbbf8-5af2-4a9d-8d2d-2dfff3513363

### Tech stack

- Jetpack Compose
- DataStore
- Room
- Ktor
- Kotlin Coroutines
- Kotlinx Serialization

## Project status

Currently, the app can only add data to a Google Sheet and extract a list of categories/values that users can choose from when inserting new data. These categories/values can be obtained by entering a custom range (following the _A1 notation_) in the Settings screen. In the Settings screen, the user can also select the spreadsheet and sheet where the data will be inserted.

The next steps include:
- implementing **unit** and **instrumented tests**;
- implementing the automation feature, which will involve reading notifications, extracting the necessary information (with the help of a regex entered by the user for each application, or a remote LLM to avoid potential breaking changes in notification formats) and finally uploading the extracted values to the sheet.

The project is still under development. To follow the progress of the app, check out the **GitHub Project** section of the repository.


## Build considerations

> To learn about developing with Google Workspace APIs, including handling authentication and authorization, refer to [Develop on Google Workspace](https://developers.google.com/workspace/guides/get-started).

Once you've got your `CLIENT_ID` and activated the right APIs (Google Sheet API and Google Drive API) in your Google Worskpace, put it in a file named `local.properties` at the root of the project:

```
CLIENT_ID="<insert_your_client_ID_between_quotation_marks>"
```

you can then build the APK in Android Studio.
