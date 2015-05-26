package by.stark.sample.webapp.page.user.provider;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import by.stark.sample.datamodel.Record4Hands;

public class ProfileDataProviderHands extends ListDataProvider<Record4Hands>
		implements ISortStateLocator<String> {

	private static final long serialVersionUID = 1L;

	private final SingleSortState<String> state = new SingleSortState<String>();

	public ProfileDataProviderHands(List<Record4Hands> list) {
		super(list);
	}

	@Override
	protected List<Record4Hands> getData() {
		List<Record4Hands> list = super.getData();
		SortParam<String> param = this.state.getSort();

		if (param != null) {
			Collections.sort(list, new RecordComparator(param.getProperty(),
					param.isAscending()));
		}

		return list;
	}

	@Override
	public ISortState<String> getSortState() {
		return this.state;
	}

	static class RecordComparator implements Comparator<Record4Hands>,
			Serializable {
		private static final long serialVersionUID = 1L;

		private final String property;
		private final boolean ascending;

		public RecordComparator(String property, boolean ascending) {
			this.property = property;
			this.ascending = ascending;
		}

		@Override
		public int compare(Record4Hands p1, Record4Hands p2) {
			Object o1 = PropertyResolver.getValue(this.property, p1);
			Object o2 = PropertyResolver.getValue(this.property, p2);

			if (o1 != null && o2 != null) {
				Comparable<Object> c1 = toComparable(o1);
				Comparable<Object> c2 = toComparable(o2);

				return c1.compareTo(c2) * (this.ascending ? 1 : -1);
			}

			return 0;
		}

		@SuppressWarnings("unchecked")
		private static Comparable<Object> toComparable(Object o) {
			if (o instanceof Comparable<?>) {
				return (Comparable<Object>) o;
			}

			throw new WicketRuntimeException("Object should be a Comparable");
		}
	}
}