# 广告屏保

一个轻量级的简单屏保功能组件，可直接用于Android终端机做广告播放

1. 支持图片和视频混播
2. 支持图片过渡动画，动画扩展，自带多种动画
3. 支持更换图片和视频的加载器

# 效果图

![](screenshot/img_1.gif)

# 引用

##Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

##Step 2. Add the dependency

	dependencies {
		        implementation 'com.github.kellysong:screensaver:1.1.0'
		}

或者引用本地lib
	
    implementation project(':screensaver-lib')

# 调用

	<com.sjl.screensaver.ScreenSaverView
        android:id="@+id/screenSaverView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />


 		 //加载广告数据
        screenSaverView.load(advModels);
        //循环播放广告
        screenSaverView.play();

# License

    Copyright 2020 Song Jiali
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.