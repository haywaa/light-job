package com.chf.lightjob.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.chf.lightjob.constants.ErrorCode;
import com.chf.lightjob.exception.BizException;

public abstract class AssertBiz {

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo() , nullSafeGet(messageSupplier));
		}
	}

	public static void isNull(@Nullable Object object, String message) {
		if (object != null) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
		if (object != null) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void notNull(@Nullable Object object, String message) {
		if (object == null) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}


	public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
		if (object == null) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void hasLength(@Nullable String text, String message) {
		if (!StringUtils.hasLength(text)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void hasLength(@Nullable String text, Supplier<String> messageSupplier) {
		if (!StringUtils.hasLength(text)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void hasText(@Nullable String text, String message) {
		if (!StringUtils.hasText(text)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void hasText(@Nullable String text, Supplier<String> messageSupplier) {
		if (!StringUtils.hasText(text)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void notEmpty(@Nullable Object[] array, String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
		if (ObjectUtils.isEmpty(array)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void noNullElements(@Nullable Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
				}
			}
		}
	}

	public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
				}
			}
		}
	}

	public static void notEmpty(@Nullable Collection<?> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	public static void notEmpty(@Nullable Map<?, ?> map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), message);
		}
	}

	public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
		if (CollectionUtils.isEmpty(map)) {
			throw new BizException(ErrorCode.INVALID_PARAM.getErrorNo(), nullSafeGet(messageSupplier));
		}
	}

	@Nullable
	private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}
}