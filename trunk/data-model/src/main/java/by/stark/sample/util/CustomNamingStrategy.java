package by.stark.sample.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {

	@Override
	public String foreignKeyColumnName(final String propertyName,
			final String propertyEntityName, final String propertyTableName,
			final String referencedColumnName) {
		final String base = super.foreignKeyColumnName(propertyName,
				propertyEntityName, propertyTableName, referencedColumnName);
		return StringUtils.isNotEmpty(base) ? base + "_id" : base;
	}

}
