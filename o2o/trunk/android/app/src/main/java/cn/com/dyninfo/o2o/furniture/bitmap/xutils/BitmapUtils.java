/*
 * Copyright (c) 2009-2016 SHENZHEN Eternal Dynasty Technology Co.,Ltd.
 * All rights reserved.
 *
 * This file contains valuable properties of  SHENZHEN Eternal Dynasty
 * Technology Co.,Ltd.,  embodying  substantial  creative efforts  and
 * confidential information, ideas and expressions.    No part of this
 * file may be reproduced or distributed in any form or by  any  means,
 * or stored in a data base or a retrieval system,  without  the prior
 * written permission  of  SHENZHEN Eternal Dynasty Technology Co.,Ltd.
 *
 */

package cn.com.dyninfo.o2o.furniture.bitmap.xutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.BitmapCacheListener;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.BitmapCommonUtils;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.BitmapDisplayConfig;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.BitmapGlobalConfig;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.callback.BitmapLoadCallBack;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.callback.BitmapLoadFrom;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.core.AsyncDrawable;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.core.BitmapSize;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.bitmap.download.Downloader;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.util.core.CompatibleAsyncTask;
import cn.com.dyninfo.o2o.furniture.bitmap.xutils.util.core.LruDiskCache;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * @Description bmp各种操作
 * @author wyouflf
 * @editor <a href="http://t.cn/RvIApP5">ceychen</a>
 * @update 2014-6-5 11:55:45
 */
public class BitmapUtils {

	private boolean pauseTask = false;
	private final Object pauseTaskLock = new Object();

	private Context context;
	private BitmapGlobalConfig globalConfig;
	private BitmapDisplayConfig defaultDisplayConfig;

	// ///////////////////////////////////////////// create
	// ///////////////////////////////////////////////////
	public BitmapUtils(Context context) {
		this(context, null);
	}

	public BitmapUtils(Context context, String diskCachePath) {
		if (context == null) {
			throw new IllegalArgumentException("context may not be null");
		}

		this.context = context;
		globalConfig = new BitmapGlobalConfig(context, diskCachePath);
		defaultDisplayConfig = new BitmapDisplayConfig();
	}

	public BitmapUtils(Context context, String diskCachePath, int memoryCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
	}

	public BitmapUtils(Context context, String diskCachePath, int memoryCacheSize, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	public BitmapUtils(Context context, String diskCachePath, float memoryCachePercent) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
	}

	public BitmapUtils(Context context, String diskCachePath, float memoryCachePercent, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	// ////////////////////////////////////// config
	// ////////////////////////////////////////////////////////////////////

	public BitmapUtils configDefaultLoadingImage(Drawable drawable) {
		defaultDisplayConfig.setLoadingDrawable(drawable);
		return this;
	}

	public BitmapUtils configDefaultLoadingImage(int resId) {
		defaultDisplayConfig.setLoadingDrawable(context.getResources().getDrawable(resId));
		return this;
	}

	public BitmapUtils configDefaultLoadingImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadingDrawable(new BitmapDrawable(context.getResources(), bitmap));
		return this;
	}

	public BitmapUtils configDefaultLoadFailedImage(Drawable drawable) {
		defaultDisplayConfig.setLoadFailedDrawable(drawable);
		return this;
	}

	public BitmapUtils configDefaultLoadFailedImage(int resId) {
		defaultDisplayConfig.setLoadFailedDrawable(context.getResources().getDrawable(resId));
		return this;
	}

	public BitmapUtils configDefaultLoadFailedImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadFailedDrawable(new BitmapDrawable(context.getResources(), bitmap));
		return this;
	}

	public BitmapUtils configDefaultBitmapMaxSize(int maxWidth, int maxHeight) {
		defaultDisplayConfig.setBitmapMaxSize(new BitmapSize(maxWidth, maxHeight));
		return this;
	}

	public BitmapUtils configDefaultBitmapMaxSize(BitmapSize maxSize) {
		defaultDisplayConfig.setBitmapMaxSize(maxSize);
		return this;
	}

	public BitmapUtils configDefaultImageLoadAnimation(Animation animation) {
		defaultDisplayConfig.setAnimation(animation);
		return this;
	}

	public BitmapUtils configDefaultAutoRotation(boolean autoRotation) {
		defaultDisplayConfig.setAutoRotation(autoRotation);
		return this;
	}

	public BitmapUtils configDefaultShowOriginal(boolean showOriginal) {
		defaultDisplayConfig.setShowOriginal(showOriginal);
		return this;
	}

	public BitmapUtils configDefaultBitmapConfig(Bitmap.Config config) {
		defaultDisplayConfig.setBitmapConfig(config);
		return this;
	}

	public BitmapUtils configDefaultDisplayConfig(BitmapDisplayConfig displayConfig) {
		defaultDisplayConfig = displayConfig;
		return this;
	}

	public BitmapUtils configDownloader(Downloader downloader) {
		globalConfig.setDownloader(downloader);
		return this;
	}

	public BitmapUtils configDefaultCacheExpiry(long defaultExpiry) {
		globalConfig.setDefaultCacheExpiry(defaultExpiry);
		return this;
	}

	public BitmapUtils configDefaultConnectTimeout(int connectTimeout) {
		globalConfig.setDefaultConnectTimeout(connectTimeout);
		return this;
	}

	public BitmapUtils configDefaultReadTimeout(int readTimeout) {
		globalConfig.setDefaultReadTimeout(readTimeout);
		return this;
	}

	public BitmapUtils configThreadPoolSize(int threadPoolSize) {
		globalConfig.setThreadPoolSize(threadPoolSize);
		return this;
	}

	public BitmapUtils configMemoryCacheEnabled(boolean enabled) {
		globalConfig.setMemoryCacheEnabled(enabled);
		return this;
	}

	public BitmapUtils configDiskCacheEnabled(boolean enabled) {
		globalConfig.setDiskCacheEnabled(enabled);
		return this;
	}

	public BitmapUtils configDiskCacheFileNameGenerator(
			LruDiskCache.DiskCacheFileNameGenerator diskCacheFileNameGenerator) {
		globalConfig.setDiskCacheFileNameGenerator(diskCacheFileNameGenerator);
		return this;
	}

	public BitmapUtils configBitmapCacheListener(BitmapCacheListener listener) {
		globalConfig.setBitmapCacheListener(listener);
		return this;
	}

	public BitmapUtils configGlobalConfig(BitmapGlobalConfig globalConfig) {
		this.globalConfig = globalConfig;
		return this;
	}

	// //////////////////////// display ////////////////////////////////////

	public <T extends View> void display(T container, String uri) {
		display(container, uri, null, null);
	}

	public <T extends View> void display(T container, String uri, BitmapDisplayConfig displayConfig) {
		display(container, uri, displayConfig, null);
	}

	public <T extends View> void display(T container, String uri, BitmapLoadCallBack<T> callBack) {
		display(container, uri, null, callBack);
	}

	public <T extends View> void display(T container, String uri, BitmapDisplayConfig displayConfig,
			BitmapLoadCallBack<T> callBack) {
		if (container == null) {
			return;
		}

		container.clearAnimation();

		if (callBack == null) {
			callBack = new DefaultBitmapLoadCallBack<T>();
		}

		if (displayConfig == null || displayConfig == defaultDisplayConfig) {
			displayConfig = defaultDisplayConfig.cloneNew();
		}

		// Optimize Max Size
		BitmapSize size = displayConfig.getBitmapMaxSize();
		displayConfig.setBitmapMaxSize(BitmapCommonUtils.optimizeMaxSizeByView(container, size.getWidth(),
				size.getHeight()));

		callBack.onPreLoad(container, uri, displayConfig);

		if (TextUtils.isEmpty(uri)) {
			callBack.onLoadFailed(container, uri, displayConfig.getLoadFailedDrawable());
			return;
		}

		Bitmap bitmap = globalConfig.getBitmapCache().getBitmapFromMemCache(uri, displayConfig);

		if (bitmap != null) {
			callBack.onLoadStarted(container, uri, displayConfig);
			callBack.onLoadCompleted(container, uri, bitmap, displayConfig, BitmapLoadFrom.MEMORY_CACHE);
		} else if (!bitmapLoadTaskExist(container, uri, callBack)) {

			final BitmapLoadTask<T> loadTask = new BitmapLoadTask<T>(container, uri, displayConfig, callBack);
			// set loading image
			final AsyncDrawable<T> asyncDrawable = new AsyncDrawable<T>(displayConfig.getLoadingDrawable(), loadTask);
			callBack.setDrawable(container, asyncDrawable);

			// load bitmap from uri or diskCache
			loadTask.executeOnExecutor(globalConfig.getBitmapLoadExecutor());
		}
	}

	// ///////////////////////////////////////////// cache
	// /////////////////////////////////////////////////////////////////

	public void clearCache() {
		globalConfig.clearCache();
	}

	public void clearMemoryCache() {
		globalConfig.clearMemoryCache();
	}

	public void clearDiskCache() {
		globalConfig.clearDiskCache();
	}

	public void clearCache(String uri) {
		globalConfig.clearCache(uri);
	}

	public void clearMemoryCache(String uri) {
		globalConfig.clearMemoryCache(uri);
	}

	public void clearDiskCache(String uri) {
		globalConfig.clearDiskCache(uri);
	}

	public void flushCache() {
		globalConfig.flushCache();
	}

	public void closeCache() {
		globalConfig.closeCache();
	}

	public File getBitmapFileFromDiskCache(String uri) {
		return globalConfig.getBitmapCache().getBitmapFileFromDiskCache(uri);
	}

	public Bitmap getBitmapFromMemCache(String uri, BitmapDisplayConfig config) {
		if (config == null) {
			config = defaultDisplayConfig;
		}
		return globalConfig.getBitmapCache().getBitmapFromMemCache(uri, config);
	}

	// //////////////////////////////////////// tasks
	// //////////////////////////////////////////////////////////////////////

	public void resumeTasks() {
		pauseTask = false;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	public void pauseTasks() {
		pauseTask = true;
		flushCache();
	}

	public void stopTasks() {
		pauseTask = true;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private static <T extends View> BitmapLoadTask<T> getBitmapTaskFromContainer(T container,
			BitmapLoadCallBack<T> callBack) {
		if (container != null) {
			final Drawable drawable = callBack.getDrawable(container);
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable<T> asyncDrawable = (AsyncDrawable<T>) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	private static <T extends View> boolean bitmapLoadTaskExist(T container, String uri, BitmapLoadCallBack<T> callBack) {
		final BitmapLoadTask<T> oldLoadTask = getBitmapTaskFromContainer(container, callBack);

		if (oldLoadTask != null) {
			final String oldUrl = oldLoadTask.uri;
			if (TextUtils.isEmpty(oldUrl) || !oldUrl.equals(uri)) {
				oldLoadTask.cancel(true);
			} else {
				return true;
			}
		}
		return false;
	}

	public class BitmapLoadTask<T extends View> extends CompatibleAsyncTask<Object, Object, Bitmap> {
		private final String uri;
		private final WeakReference<T> containerReference;
		private final BitmapLoadCallBack<T> callBack;
		private final BitmapDisplayConfig displayConfig;

		private BitmapLoadFrom from = BitmapLoadFrom.DISK_CACHE;

		public BitmapLoadTask(T container, String uri, BitmapDisplayConfig config, BitmapLoadCallBack<T> callBack) {
			if (container == null || uri == null || config == null || callBack == null) {
				throw new IllegalArgumentException("args may not be null");
			}

			this.containerReference = new WeakReference<T>(container);
			this.callBack = callBack;
			this.uri = uri;
			this.displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {

			synchronized (pauseTaskLock) {
				while (pauseTask && !this.isCancelled()) {
					try {
						pauseTaskLock.wait();
					} catch (Throwable e) {
					}
				}
			}

			Bitmap bitmap = null;

			// get cache from disk cache
			if (!this.isCancelled() && this.getTargetContainer() != null) {
				this.publishProgress(PROGRESS_LOAD_STARTED);
				bitmap = globalConfig.getBitmapCache().getBitmapFromDiskCache(uri, displayConfig);
			}

			// download image
			if (bitmap == null && !this.isCancelled() && this.getTargetContainer() != null) {
				bitmap = globalConfig.getBitmapCache().downloadBitmap(uri, displayConfig, this);
				from = BitmapLoadFrom.URI;
			}

			return bitmap;
		}

		public void updateProgress(long total, long current) {
			this.publishProgress(PROGRESS_LOADING, total, current);
		}

		private static final int PROGRESS_LOAD_STARTED = 0;
		private static final int PROGRESS_LOADING = 1;

		@Override
		protected void onProgressUpdate(Object... values) {
			if (values == null || values.length == 0)
				return;

			final T container = this.getTargetContainer();
			if (container == null)
				return;

			switch ((Integer) values[0]) {
			case PROGRESS_LOAD_STARTED:
				callBack.onLoadStarted(container, uri, displayConfig);
				break;
			case PROGRESS_LOADING:
				if (values.length != 3)
					return;
				callBack.onLoading(container, uri, displayConfig, (Long) values[1], (Long) values[2]);
				break;
			default:
				break;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			final T container = this.getTargetContainer();
			if (container != null) {
				if (bitmap != null) {
					callBack.onLoadCompleted(container, this.uri, bitmap, displayConfig, from);
				} else {
					callBack.onLoadFailed(container, this.uri, displayConfig.getLoadFailedDrawable());
				}
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			synchronized (pauseTaskLock) {
				pauseTaskLock.notifyAll();
			}
		}

		public T getTargetContainer() {
			final T container = containerReference.get();
			final BitmapLoadTask<T> bitmapWorkerTask = getBitmapTaskFromContainer(container, callBack);

			if (this == bitmapWorkerTask) {
				return container;
			}

			return null;
		}
	}

}
