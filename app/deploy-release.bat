SET ADB=d:\android-sdk\platform-tools\adb.exe
SET CURR_PATH=%~dp0
%ADB% uninstall cn.sdgundam.comicatsdgo
%ADB% install %CURR_PATH%release\app-release.apk