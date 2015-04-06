package by.stark.sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomData;
import org.apache.commons.math3.random.RandomDataImpl;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.stark.sample.datamodel.Author;
import by.stark.sample.datamodel.Book;
import by.stark.sample.datamodel.Comment;
import by.stark.sample.datamodel.Ebook;
import by.stark.sample.datamodel.Genre;
import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Picture;
import by.stark.sample.datamodel.Publisher;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.CommentRating;
import by.stark.sample.datamodel.enums.RecordStatus;
import by.stark.sample.datamodel.enums.UserGender;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public abstract class AbstractServiceTest {

	private final static Random random = new Random();
	protected static final RandomData RANDOM_DATA = new RandomDataImpl();

	private static final int RANDOM_STRING_SIZE = 8;

	public static String randomString() {
		return RandomStringUtils.randomAlphabetic(RANDOM_STRING_SIZE);
	}

	public static String randomString(final String prefix) {
		return String.format("%s-%s", new Object[] { prefix, randomString() });
	}

	public static int randomTestObjectsCount() {
		return RANDOM_DATA.nextInt(0, 5) + 1;
	}

	public static int randomInteger() {
		return randomInteger(0, 9999);
	}

	public static int randomInteger(final int lower, final int upper) {
		return RANDOM_DATA.nextInt(lower, upper);
	}

	public static boolean randomBoolean() {
		return Math.random() < 0.5;
	}

	public static long randomLong() {
		return RANDOM_DATA.nextLong(0, 9999999);
	}

	public static BigDecimal randomBigDecimal() {
		return new BigDecimal(randomDouble()).setScale(2, RoundingMode.HALF_UP);
	}

	public static double randomDouble() {
		final double value = random.nextDouble() + randomInteger();
		return Math.round(value * 1e2) / 1e2;

	}

	public static <T> T randomFromCollection(final Collection<T> all) {
		final int size = all.size();
		final int item = new Random().nextInt(size); // In real life, the Random
														// object should be
														// rather more shared
														// than this
		int i = 0;
		for (final T obj : all) {
			if (i == item) {
				return obj;
			}
			i = i + 1;
		}
		return null;
	}

	public static Date randomDate() {
		final int year = randBetween(1900, 2010);
		final GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, year);
		final int dayOfYear = randBetween(1,
				gc.getActualMaximum(Calendar.DAY_OF_YEAR));
		gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return gc.getTime();
	}

	public static int randBetween(final int start, final int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	// ---create tables---

	protected Author createAuthor() {
		Author author = new Author();
		author.setFirstName(randomString("firstName-"));
		author.setLastName(randomString("lastName-"));
		return author;
	}

	protected Genre createGenre() {
		Genre genre = new Genre();
		genre.setName(randomString("name-"));
		return genre;
	}

	protected Publisher createPublisher() {
		Publisher publisher = new Publisher();
		publisher.setName(randomString("name-"));
		return publisher;
	}

	protected Picture createPicture() {
		Picture picture = new Picture();
		picture.setName(randomString("name-"));
		return picture;
	}

	protected Book createBook() {
		Book book = new Book();
		book.setDescription(randomString("description-"));
		book.setIsbn(randomString("isbn-"));
		book.setPages(randomLong());
		book.setPicture(createPicture());
		book.setPublisher(createPublisher());
		book.setTitle(randomString("title-"));
		book.setYear(randomLong());
		return book;
	}

	protected Ebook createEbook() {
		Ebook ebook = new Ebook();
		ebook.setBook(createBook());
		ebook.setName(randomString("name-"));
		return ebook;
	}

	protected Libriary createLibriary() {
		Libriary libriary = new Libriary();
		libriary.setAvailability(randomBoolean());
		libriary.setBook(createBook());
		libriary.setReadingRoom(randomBoolean());
		libriary.setUin(randomLong());
		return libriary;
	}

	protected Userprofile createUser() {
		Userprofile userprofile = new Userprofile();
		userprofile.setBirthday(randomDate());
		userprofile.setEmail(randomString("email-"));
		userprofile.setFirstName(randomString("firstName-"));
		userprofile.setGender(randomFromCollection(Arrays.asList(UserGender
				.values())));
		userprofile.setLastName(randomString("lastName-"));
		userprofile.setPassword(randomString("password-"));
		userprofile.setPicture(createPicture());
		userprofile.setRole(randomFromCollection(Arrays.asList(UserRole
				.values())));
		userprofile.setStatus(randomFromCollection(Arrays.asList(UserStatus
				.values())));
		return userprofile;
	}

	protected Comment createComment() {
		Comment comment = new Comment();
		comment.setBook(createBook());
		comment.setDescription(randomString("description-"));
		comment.setRating(randomFromCollection(Arrays.asList(CommentRating
				.values())));
		comment.setUser(createUser());
		return comment;
	}

	protected Record4Hands createRecord4Hands() {
		Record4Hands record4hands = new Record4Hands();
		record4hands.setDateReturn(randomDate());
		record4hands.setDateTake(randomDate());
		record4hands.setDescription(randomString("description-"));
		record4hands.setLibriary(createLibriary());
		record4hands.setStatus(randomFromCollection(Arrays.asList(RecordStatus
				.values())));
		record4hands.setUser(createUser());
		return record4hands;
	}

	protected Record4Room createRecord4Room() {
		Record4Room record4room = new Record4Room();
		record4room.setTimeReturn(randomDate());
		record4room.setTimeTake(randomDate());
		record4room.setDescription(randomString("description-"));
		record4room.setLibriary(createLibriary());
		record4room.setStatus(randomFromCollection(Arrays.asList(RecordStatus
				.values())));
		record4room.setUser(createUser());
		return record4room;
	}
}