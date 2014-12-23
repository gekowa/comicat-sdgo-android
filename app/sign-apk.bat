SET CURR_PATH=%~dp0
jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 -storepass 222222 -keystore %CURR_PATH%keystore\comicat_default.jks -signedjar %CURR_PATH%release\comicat-sdgo.apk %CURR_PATH%release\app-release.apk "comicat default"
