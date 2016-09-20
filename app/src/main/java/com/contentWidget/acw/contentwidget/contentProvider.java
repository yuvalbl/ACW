package com.contentWidget.acw.contentwidget;

import java.util.Date;

/**
 * Work as a POJO for GJON parser (match server JSON structure)
 */
public class contentProvider {
    Provider provider;
    Date lastUpdated;
}

class Provider {
    int id;
    String token;
}
