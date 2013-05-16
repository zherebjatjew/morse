package com.android.dj.flow;

import android.util.Log;
import com.google.common.collect.ListMultimap;
import org.atteo.evo.classindex.ClassIndex;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class Director {
	private static final String TAG = "Director";

	private ListMultimap<Class, Class> listeners;
	private ThreadPoolExecutor pool;

	public Director(int maxThreads) {
		pool = new ThreadPoolExecutor(1, maxThreads, 400, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		for (Class<?> clazz : ClassIndex.getAnnotated(FlowListener.class)) {
			Class messageClass = clazz.getAnnotation(FlowListener.class).value();
			if (messageClass.asSubclass(Runnable.class) == null) {
				Log.w(TAG, "Class " + clazz.getName() + " is annotated by @FlowListener but does not implement Runnable");
			} else {
				try {
					clazz.getConstructor(Director.class, messageClass);
					listeners.put(messageClass, clazz);
					Log.v(TAG, messageClass.getSimpleName() + " => " + clazz.getSimpleName());
				} catch (NoSuchMethodException e) {
					Log.w(TAG, "Class " + clazz.getName() + " does not have required constructor "
							+ clazz.getSimpleName() + "(Director director, " + messageClass.getSimpleName() + " message)");
				}
			}
		}
	}

	public void post(Object message) {
		if (message == null) return;
		if (pool.isTerminating()) return;
		for (Class clazz : listeners.get(message.getClass())) {
			try {
				Constructor constructor = clazz.getConstructor(Director.class, message.getClass());
				pool.execute((Runnable) constructor.newInstance(this, message));
			} catch (NoSuchMethodException e) {
				Log.wtf(TAG, "Required constructor not found in " + clazz.getName());
			} catch (InvocationTargetException e) {
				Log.e(TAG, "Error instantiating " + clazz.getName(), e);
			} catch (InstantiationException e) {
				Log.e(TAG, "Error instantiating " + clazz.getName(), e);
			} catch (IllegalAccessException e) {
				Log.e(TAG, "Error instantiating " + clazz.getName(), e);
			}
		}
	}

	public void terminate() {
		pool.shutdown();
	}
}
