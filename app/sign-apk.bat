SET CURR_PATH=%~dp0
"C:\Program Files\Java\jdk1.8.0_20\bin\jarsigner" -verbose -sigalg MD5withRSA -digestalg SHA1 -storepass 222222 -keystore %CURR_PATH%keystore\comicat_default.jks -signedjar %CURR_PATH%release\comicat-sdgo.apk %CURR_PATH%release\cn.sdgundam.comicatsdgo.encrypted.apk "comicat default"
