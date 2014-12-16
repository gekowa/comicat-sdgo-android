SET ADB=d:\android-sdk\platform-tools\adb.exe
SET mypath=%~dp0
%ADB% uninstall cn.sdgundam.comicatsdgo
%ADB% install %mypath%release\app-release.apk