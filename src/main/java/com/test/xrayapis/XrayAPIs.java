package com.test.xrayapis;

public class XrayAPIs {
	private XrayAPIs() {

	}

	public static final String TEST_EXECUTION_GET_URL = "/rest/raven/1.0/api/testexec/execid/test?detailed=true";
	public static final String TEST_RUN_STATUS_PUT_URL = "/rest/raven/1.0/api/testrun/id/status";
	public static final String TEST_RUN_GET_URL = "/rest/raven/1.0/api/testrun";
	public static final String UPDATE_ISSUE_URL="/rest/api/2/issue/issueId/transitions";
	public static final String GET_ISSUE_URL="/rest/api/latest/search?jql=project=projectName&issuetype=Bug&fields=id,key,description,summary&maxResults=";
	public static final String GET_ISSUE="/rest/api/2/issue/issueId?fields=status";

}
