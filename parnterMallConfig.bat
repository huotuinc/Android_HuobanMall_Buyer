@echo off
rem --文档说明----
rem 批量替换/拷贝伙伴商城买家版资源文件
rem 资源文件包括:
rem 2、商户信息配置文件
rem 3、color.xml文件
rem 4、style.xml文件
rem 5、APP图标
rem 6、引导图片
rem 7、key.keystore文件
rem 8、启动图片
rem 9、菜单图标
rem 10、动画文件
rem 由Aaron操刀编写
rem 2015-08-27
rem windows下专用。liunx、ios用户敬请期待.sh
rem 全部资源文件约定在D:\resources目录
rem ----文件根目录----
rem 资源文件根目录
set RESOURCES_DIR = "D://resources//"
rem android项目根目录
set ANDROID_DIR = D:\program\code\Android_HuobanMall_Buyer
rem -----RES模板文件设置----
rem RES模板文件夹目录
set RES_TEMPLATE = "%ANDROID_DIR%\res"
rem -----------发布渠道设定-------
rem 渠道名
set CHANNEL_NAME = yaoshengji
rem 渠道路径
set CHANNEL_DIR = "%ANDROID_DIR%\custom\%CHANNEL_NAME%"
rem Manifest文件路径
set MANIFEST_DIR = "%ANDROID_DIR%"
rem Manifest文件名称
set MANIFEST_NAME = AndroidManifest.xml
rem -----商户信息配置文件---
rem 商户信息配置文件目录待配置
set MERCHANT_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\xml"
rem 商户信息配置文件目录本地
set MERCHANT_DIR_LOCAL= "%RESOURCES_DIR%\xml"
rem 商户信息文件名称
set MERCHANT_FILE_NAME = merchant_info.xml
rem ----颜色、样式配置文件---
rem color.xml style.xml文件的目录
set COLOR_STYLE_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\values"
rem color.xml style.xml文件的目录本地
set COLOR_STYLE_DIR_LOCAL = "%RESOURCES_DIR%\values"
rem color文件名称
set COLOR_NAME = color.xml
rem style文件名
set STYLE_NAME = style.xml
rem -----keystore配置文件-
rem keystore文件目录
set KEYSTORE_DIR_ANDROID = "%ANDROID_DIR%\keystore"
rem keystore文件目录本地
set KEYSTORE_DIR_LOCAL = "%RESOURCES_DIR%\keystore"
rem keystore文件名称
set KEYSTORE_NAME = key.keystore
rem ----------动画文件配置----
rem 动画文件目录
set ANIM_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\anim"
rem 动画文件目录本地
set ANIM_DIR_LOCAL = "%RESOURCES_DIR%\anim"
rem ----图片文件配置----
rem mdpi分辨率图片
rem 图片目录-mdpi
set IMAGE_MDPI_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\drawable-mdpi"
rem 图片目录-mdpi本地
set IMAGE_MDPI_DIR_LOCAL = "%RESOURCES_DIR%\drawable-mdpi"
rem 图片目录-hdpi
set IMAGE_HDPI_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\drawable-hdpi"
rem 图片目录-hdpi本地
set IMAGE_HDPI_DIR_LOCAL = "%RESOURCES_DIR%\drawable-hdpi"
rem 图片目录-xhdpi
set IMAGE_XHDPI_DIR_ANDROID = "%ANDROID_DIR%\custom\%CHANNEL_NAME%\res\drawable-xhdpi"
rem 图片目录-xhdpi本地
set IMAGE_XHDPI_DIR_LOCAL = "%RESOURCES_DIR%\drawable-xhdpi"
rem ----自定义参数结束------
rem -----打包渠道文件配置----
rem 判断打包渠道是否存在
if exist "%CHANNEL_DIR%" goto copyres
if not exist "%CHANNEL_DIR%" goto creatchannel
:copyres
echo "小娜已经开始拷贝资源模板文件，请稍后..."


pause
:creatchannel
echo "小娜已经开始创建通道文件，请稍后..."
rem 创建通道文件
echo "%RESOURCES_DIR%"
md "%CHANNEL_DIR%"

rem 拷贝Manifest文件
echo "小娜开始拷贝%MANIFEST_NAME%文件"
copy /y %MANIFEST_DIR%/%MANIFEST_NAME% %CHANNEL_DIR%/
echo "小娜已经拷贝完成了%MANIFEST_NAME%文件"
echo "小娜开始拷贝res资源模板文件夹"
set name = %~n1
if not exist  %CHANNEL_DIR%/%name%/ md %CHANNEL_DIR%/%name%/
xcopy %1 %CHANNEL_DIR%/%name%/ /c/q/e
echo "小娜已经完成了拷贝res资源模板文件夹"
pause