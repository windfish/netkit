# java中的事件机制

### 事件机制有三种角色

1. event object： 事件状态对象，用于listener 的相应的方法之中，作为参数
2. event source： 具体的事件源，比如说，点击button，那么button 就是event source，要想button 对点击事件进行响应，就需要注册特定的listener
3. event listener： 对每一个明确事件的发生，都相应的定义一个明确的方法。这些方法都集中定义在事件监听者（继承 java.util.EventListener接口）中

伴随着事件的发生，相应的状态通常都封装在事件状态对象中，该对象必须继承自java.util.EventObject。
事件状态对象作为单参传递给应响应该事件的监听者方法中。发出某种特定事件的事件源的标识是：遵从规定的设计格式为事件监听者定义注册方法，并接受对指定事件监听者接口实例的引用。
具体的对监听的事件类，当它监听到event object产生的时候，它就调用相应的方法，进行处理。


