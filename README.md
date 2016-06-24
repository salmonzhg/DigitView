# DigitView
一个滚动数字的控件

![](https://github.com/billy96322/DigitView/blob/master/app/screen_apture/digitalview.gif "demo")

###特性
* 基于属性动画实现，如需兼容2.3需修改使用[NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)
* 可以动态修改颜色，字体大小，间距等
* 可以在activity中动态创建

###已知缺陷
* 位数一旦设置则固定下来，不能根据设置的数字位数来动态变化。修改位数将重置数字
* 初始的数字只能是若干个0，暂不支持设置初始值
* 动态设置字体大小可能导致字体位置在Y轴上产生偏移

###使用
在布局文件中添加
```
<com.salmonzhg.digitview.views.DigitalGroupView
    android:id="@+id/digital"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerHorizontal="true"
    app:digiGroupColor="@color/colorAccent"
    app:digiGroupFigureCounts="5"
    app:digiGroupInterval="1dp"
    app:digiGroupTextSize="16sp"
    android:layout_marginTop="30dp"/>
```
以下参数分变为数字的颜色，位数，间距，大小
```
app:digiGroupColor="@color/colorAccent"
app:digiGroupFigureCounts="5" 
app:digiGroupInterval="1dp" 
app:digiGroupTextSize="16sp"
```
在Activity中添加
```Java
digitalGroupView.setDigits(num);
```
即可触发动画。如果转入的参数为负数，将取绝对值。如果需要在Activity中动态添加，添加如下代码。
```Java
DigitalGroupView view = new DigitalGroupView(this);
view.setTextSize(14);
view.setFigureCount(3);
view.setInterval(5);
view.setColor(Color.BLACK);
viewGroup.addView(view);
```

###源码
[GitHub](https://github.com/billy96322/DigitView)
