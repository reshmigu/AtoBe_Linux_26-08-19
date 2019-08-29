package com.test.xrayapis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GenerateJasperReport {
	private static final Logger LOGGER = LogManager.getLogger(GenerateJasperReport.class);

	public void createReport(JasperReportDTO jasperReportDTO, List<JasperBugDTO> jasperBugDTOList) throws JRException {
		// Compile jrxml file.
		JasperReport jasperReport = JasperCompileManager.compileReport("IssueReport.jrxml");

		// Parameters for report
		int totalTestCases = 0;
		HashMap<String, Object> parameters = new HashMap<>();
		if (jasperBugDTOList != null && !jasperBugDTOList.isEmpty()) {
			totalTestCases = jasperBugDTOList.size();
		}
		if (jasperBugDTOList != null) {
			int passCount = (int) jasperBugDTOList.stream().filter(a -> a.getTestStatus().equalsIgnoreCase("PASS"))
					.count();
			int failCount = (int) jasperBugDTOList.stream().filter(a -> a.getTestStatus().equalsIgnoreCase("FAIL"))
					.count();
			int bugCount = (int) jasperBugDTOList.stream().filter(a -> a.getBugLink() != null).count();
		
		parameters.put("projectName", jasperReportDTO.getProjectName());
		parameters.put("issueId", jasperReportDTO.getIssueId());
		parameters.put("summary", jasperReportDTO.getSummary());
		parameters.put("description", jasperReportDTO.getDescription());
		parameters.put("startedDate", jasperReportDTO.getStartedDate());
		parameters.put("endDate", jasperReportDTO.getEndDate());
		parameters.put("executedBy", jasperReportDTO.getExecutedBy());
		parameters.put("assignee", jasperReportDTO.getAssignee());
		parameters.put("totalTestCases", totalTestCases);
		parameters.put("passCount", passCount);
		parameters.put("failCount", failCount);
		parameters.put("bugCount", bugCount);
		parameters.put("xrayLink", jasperReportDTO.getXrayLink());
		parameters.put("issueLink", jasperReportDTO.getIssueIdLink());
		}
		// parameters.put("SUBREPORT_DIR",
		// "C:/Users/nasia.t/JaspersoftWorkspace/MyReports/");
		/*
		 * jasperBugDTOList.forEach(bugDTO -> { parameters.put("caseId",
		 * bugDTO.getTestCaseId()); parameters.put("testStatus",
		 * bugDTO.getTestStatus()); parameters.put("bug", bugDTO.getLinkedBugId());
		 * 
		 * });
		 */
		/*
		 * jasperBugDTOList.forEach(a->{
		 * System.out.println(a.getBugLink()+"*********************************");
		 * System.out.println(a.getTestCaseId()+"!!!!!!!!!!!!!!"); });
		 */

		JRBeanCollectionDataSource bugList = new JRBeanCollectionDataSource(jasperBugDTOList);
		parameters.put("BugList", bugList);

		// JRBeanCollectionDataSource beanEnvDocument21 = new
		// JRBeanCollectionDataSource(jasperBugDTO);
		// parameters.put("subimages", beanEnvDocument21);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		// Make sure the output directory exists.
		/*
		 * File outDir = new File("C:/jasperoutput"); outDir.mkdirs();
		 */

		// Export to PDF.
		try {
			JasperExportManager.exportReportToPdfFile(jasperPrint, "test-output/report.pdf");
			// JasperExportManager.exportReportToHtmlFile( "report.pdf", "report.html");
			/*
			 * File pdf = new File("report.pdf"); //pdf.mkdirs();
			 * 
			 * JasperExportManager.exportReportToPdfStream(jasperPrint, new
			 * FileOutputStream(pdf));
			 */
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		LOGGER.info("Done!");
		LOGGER.info(LocalDateTime.now(ZoneId.of("Asia/Kolkata")) + " ************* ");
	}

}
