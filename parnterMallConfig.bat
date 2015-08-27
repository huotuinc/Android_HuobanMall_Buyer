@echo off
rem 批量替换/拷贝伙伴商城买家版资源文件
rem 资源文件包括：
rem 1、自定义的AndroidManifest.xml文件
rem 2、商户信息配置文件
rem 3、color.xml文件
rem 4、style.xml文件
rem 5、APP图标
rem 6、引导图片
rem 7、gradle.build文件
rem 8、启动图片
rem 9、菜单图标
rem 10、动画文件
rem 由Aaron操刀编写
rem 2015-08-27
rem windows下专用。liunx、ios用户敬请期待.sh
rem 全部资源文件约定在D:\resources目录

rem 资源文件根目录
set RESOURCES_DIR = "D:\resources"
rem android项目根目录
set ANDROID_DIR = "D:\program\code\Android_HuobanMall_Buyer"
rem 自定义AndroidManifest.xml文件的目录
set MANIFEST_DIR = "%ANDROID_DIR%\custom\channel\"
rem 商户信息配置文件目录
set MERCHANT_DIR = "%ANDROID_DIR%\res\xml"
rem 商户信息文件名称
set MERCHANT_FILE_NAME = "merchant_info.xml"
rem color.xml style.xml文件的目录
set COLOR_STYLE_DIR = "%ANDROID_DIR%\res\values"
rem 图片目录-mdpi
set 