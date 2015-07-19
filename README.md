# TimerView

[ENGLISH README](README_ENGLISH.md)


## 重要的事情
<br/>
由于使用了MaskFilter，辉光效果必须[关闭硬件加速](http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/)！！

由于使用了MakskFilter，辉光效果必须[关闭硬件加速](http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/)！！

由于使用了MakskFilter，辉光效果必须[关闭硬件加速](http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/)！！




## 简介
<br/>
一个倒计时的View,

可以实现最大6小时的倒计时，具体看图～




## 展示
<br/>
控件长这样，下方的button不属于这个控件

![屏幕截图](/read_me/screen_shot.png)


<br/><br/>



👇👇👇👇👇👇👇👇👇👇这儿有个gif图，等一会或者刷新～👇👇👇👇👇👇👇👇👇👇


![gif图，加载好慢...](/read_me/screen_record.gif)

👆👆👆👆👆👆👆👆👆👆这儿有个gif图，等一会或者刷新～👆👆👆👆👆👆👆👆👆👆



## 使用

### 方法1:

添加以下代码到build.gradle(Project:xxxxx):

 ```
repositories {
	    maven { url "https://jitpack.io" }
}

```

添加以下代码到build.gradle(Module:xxxxx):

 ```
dependencies {
	    compile 'com.github.pheynix:TimerView:660400fb64'
}
```

### 方法2:

找到[MyTimer.java](/app/src/main/java/com/pheynix/forthewatch/MyTimer.java)，复制到你的项目里面，就可以使用或者修改了～




## 更新


* 20150716 增加listener
* 20150717 整合@wsk900906 fork的版本，增加计时功能，新增listener
* 20150719 增加lib方便使用

----


如果对这么控件有啥问题或建议，尽情提issues或者[发邮件](mailto:pheynixdu@gmail.com)给我pheynixdu@gmail.com


<br/><br/><br/>
其实我是真的忙...

一个人写了好久代码，想找群小伙伴了

深圳/广州找工作ing...
