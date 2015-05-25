package by.stark.sample.webapp.app;

import javax.inject.Inject;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import by.stark.sample.webapp.page.home.book.BookPage;
import by.stark.sample.webapp.page.login.LoginPage;

@Component("wicketWebApplicationBean")
public class WicketWebApplication extends AuthenticatedWebApplication {

	public static final String LOGIN_URL = "/login";

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();

		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, getApplicationContext()));

		final BeanValidationConfiguration beanValidationConfiguration = new BeanValidationConfiguration();
		beanValidationConfiguration.configure(this);

		getSecuritySettings().setAuthorizationStrategy(
				new AnnotationsRoleAuthorizationStrategy(this));
		getStoreSettings().setMaxSizePerSession(Bytes.kilobytes(500));
		getStoreSettings().setInmemoryCacheSize(50);
		if (RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType())) {
			getDebugSettings().setDevelopmentUtilitiesEnabled(false);
			getDebugSettings().setComponentUseCheck(false);
			getDebugSettings().setAjaxDebugModeEnabled(false);
			getMarkupSettings().setStripComments(true);
			getMarkupSettings().setCompressWhitespace(true);
			getMarkupSettings().setStripWicketTags(true);
		} else {
			getComponentPostOnBeforeRenderListeners().add(
					new StatelessChecker());
			getDebugSettings().setDevelopmentUtilitiesEnabled(true);
			getDebugSettings().setComponentUseCheck(true);
			getDebugSettings().setAjaxDebugModeEnabled(true);
			getMarkupSettings().setStripComments(false);
			getMarkupSettings().setCompressWhitespace(true);
			getMarkupSettings().setStripWicketTags(true);
		}

		mountPage(LOGIN_URL, LoginPage.class);

		mountResource("/pictures/${name}", new ImageResourceReference());
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return BasicAuthenticationSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	public final Class<? extends WebPage> getHomePage() {
		return BookPage.class;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
