package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.Service;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
interface ServiceManagerBridge {
    ImmutableMultimap<Service.State, Service> servicesByState();
}
