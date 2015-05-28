/*
 * Copyright (C) Telly, Inc. and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codetail.graphics.compat;

import android.graphics.PorterDuffColorFilter;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.lang.reflect.Method;

import static android.graphics.PorterDuff.Mode;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Stolen
 *
 * @hide
 */
public class DrawableReflectiveUtils {
    private final static String LOGTAG = "DrawableReflectiveUtils";

    static final boolean LOLLIPOP_PLUS = SDK_INT >= LOLLIPOP;

    static SimpleArrayMap<String, Method> sCachedMethods = new SimpleArrayMap<>();

    final static Class[] INT_ARG = {int.class};

    @SuppressWarnings("unchecked")
    public static <T> T tryInvoke(Object target, String methodName, Class<?>[] argTypes, Object... args) {

        try {
            Method method = sCachedMethods.get(methodName);
            if (method != null) {
                return (T) method.invoke(target, args);
            }

            method = target.getClass().getDeclaredMethod(methodName, argTypes);
            sCachedMethods.put(methodName, method);

            return (T) method.invoke(target, args);
        } catch (Exception pokemon) {
            Log.e(LOGTAG, "Unable to invoke " + methodName + " on " + target, pokemon);
        }

        return null;
    }

    public static PorterDuffColorFilter setColor(PorterDuffColorFilter cf, int color, Mode mode) {
        if (!LOLLIPOP_PLUS) {
            return new PorterDuffColorFilter(color, mode);
        }

        tryInvoke(cf, "setColor", INT_ARG, color);
        return cf;
    }

}