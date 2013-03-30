package com.google.gerrit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.revwalk.RevCommit;

import pl.umk.bugclassification.ClassificationService;

import com.google.gerrit.extensions.annotations.Listen;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.config.GerritServerConfig;

import com.google.gerrit.server.events.CommitReceivedEvent;
import com.google.gerrit.server.git.validators.CommitValidationException;
import com.google.gerrit.server.git.validators.CommitValidationListener;
import com.google.gerrit.server.git.validators.CommitValidationMessage;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Listen
@Singleton
public class AddCommitListener implements CommitValidationListener {
	private final static String BUG_CLASSIFICATOR_SECTION = "bugclassification";
	private final static String ENDPOINT_ADDRESS_KEY = "endpointAddressKey";

	private final Config config;
	private final String endpointAddress;
	private final ClassificationService service;

	@Inject
	public AddCommitListener(@GerritServerConfig Config gerritConfig)
			throws ConfigInvalidException, IOException {
		this.config = gerritConfig;
		this.endpointAddress = config.getString(BUG_CLASSIFICATOR_SECTION,
				null, ENDPOINT_ADDRESS_KEY);
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(ClassificationService.class);
//		factory.setAddress(endpointAddress);
		factory.setAddress("http://localhost:8000/classificationService");
		service = (ClassificationService) factory.create();
	}

	@Override
	public List<CommitValidationMessage> onCommitReceived(
			CommitReceivedEvent receiveEvent) throws CommitValidationException {
		final Project project = receiveEvent.project;
		final RevCommit commit = receiveEvent.commit;
		final AbbreviatedObjectId id = commit.abbreviate(7);
		
		List<CommitValidationMessage> messages = new ArrayList<CommitValidationMessage>();
		
		List<String> content = new ArrayList<String>();
		for (String s : commit.getFullMessage().split("\n")) {
			content.add(s);
		}
		boolean result = service.classificateCommit(project.getName(), content);

		messages.add(new CommitValidationMessage("(W) " + id.name()
				+ "service.classificateCommit returned " + result, false));

		return messages;
	}
}