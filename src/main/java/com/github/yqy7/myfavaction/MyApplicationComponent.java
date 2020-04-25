package com.github.yqy7.myfavaction;

import com.intellij.openapi.components.ApplicationComponent;

/**
 * 暂时禁止动态插件，开发过程中总是在indexing的时候导致插件消失
 */
public class MyApplicationComponent implements ApplicationComponent {

}
