package com.mes.loco.aps.server.utils.aps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.excel.ExcelColorCol;
import com.mes.loco.aps.server.service.po.excel.MyExcelSheet;
import com.mes.loco.aps.server.service.po.oms.OMSCheckMsg;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.utils.CalendarUtil;
import com.mes.loco.aps.server.service.utils.Configuration;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.Constants;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

//定义导出操作
public class ExcelUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	// 1.创建一个excel文件
	static HSSFWorkbook mWorkbook;// 工作簿

	// 2.创建sheet文件
	static Sheet mSheet;

	// 3.设置头信息(第一行的数据)
	static String[] mHeads;// ={"","",""}

	/**
	 * 通用表头样式
	 * 
	 * @return
	 */
	private static CellStyle HeaderStyle() {
		CellStyle wStyle = mWorkbook.createCellStyle();
		try {
			wStyle.setAlignment(HorizontalAlignment.CENTER);
			wStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			wStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			wStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			wStyle.setWrapText(true);
			wStyle.setBorderLeft(BorderStyle.THIN); // 左边框
			wStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderTop(BorderStyle.THIN); // 上边框
			wStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderBottom(BorderStyle.THIN); // 下边框
			wStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderRight(BorderStyle.THIN); // 右边框
			wStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			Font wFont = mWorkbook.createFont();
			// 字体颜色
			wFont.setColor(IndexedColors.WHITE.getIndex());
			wFont.setBold(true);
			wFont.setFontName("Couries New");
			wFont.setFontHeightInPoints((short) 15);
			wStyle.setFont(wFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wStyle;
	}

	/**
	 * 通用行单元格样式
	 * 
	 * @return
	 */
	private static CellStyle RowStyle() {
		CellStyle wStyle = mWorkbook.createCellStyle();
		try {
			wStyle.setAlignment(HorizontalAlignment.CENTER);
			wStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			wStyle.setWrapText(true);
			wStyle.setBorderLeft(BorderStyle.THIN); // 左边框
			wStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderTop(BorderStyle.THIN); // 上边框
			wStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderBottom(BorderStyle.THIN); // 下边框
			wStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			wStyle.setBorderRight(BorderStyle.THIN); // 右边框
			wStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wStyle;
	}

	// 6.导出
	public static void Export(OutputStream wOutput) {
		try {
			// 设置导出时使用表格方式导出
//			mSheet.setGridsPrinted(true);
			mWorkbook.write(wOutput);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	// 导出订单列表相关函数
	public static CellStyle mHeaderStyle = null;
	public static CellStyle mRowStyle = null;

	public static void PO_CreateHeaders(String[] wHeaders) {
		try {
			// ①创建工作簿
			mWorkbook = new HSSFWorkbook();
			// ②定义样式
			mHeaderStyle = HeaderStyle();
			mRowStyle = RowStyle();
			// ③创建行
			Row wRow = mSheet.createRow(1);
			wRow.setHeight((short) 350);
			// ④依次添加列
			mHeads = wHeaders;
			Cell wCell;
			for (int i = 0; i < mHeads.length; i++) {
				wCell = wRow.createCell(i);
				wCell.setCellValue(mHeads[i]);
				wCell.setCellStyle(mHeaderStyle);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public static void PO_CreateCols(int wRowNum, List<String> wColList) {
		try {
			// ①创建行
			Row wRow = mSheet.createRow(wRowNum);
			// ②创建列
			Cell wCell;
			for (int i = 0; i < wColList.size(); i++) {
				wCell = wRow.createCell(i);
				wCell.setCellValue(wColList.get(i));
				wCell.setCellStyle(mRowStyle);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public static CellStyle mTitleStyle = null;
	public static CellStyle mHeadStyle = null;
	public static CellStyle mHeadStyle_Copy = null;
	public static CellStyle mItemStyle = null;

	public static CreationHelper mCreateHelper = null;
	public static CellStyle mLinkStyle = null;

	public static CellStyle mItemStyle_Green = null;
	public static CellStyle mItemStyle_Blue = null;
	public static CellStyle mItemStyle_Red = null;

	private static CellStyle TitleStyle() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();
			hSSFFont.setBold(true);
			hSSFFont.setFontName("Couries New");
			hSSFFont.setFontHeightInPoints((short) 15);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle HeadStyle() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 背景颜色
			hSSFCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();

			hSSFFont.setColor(IndexedColors.WHITE.getIndex());
			hSSFFont.setBold(true);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle HeadStyleCopy() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 背景颜色
			hSSFCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
			hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();

			hSSFFont.setColor(IndexedColors.WHITE.getIndex());
			hSSFFont.setBold(true);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle ItemStyleBlue() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// 背景颜色
			hSSFCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();

			hSSFFont.setColor(IndexedColors.WHITE.getIndex());
			hSSFFont.setBold(true);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle ItemStyleGreen() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// 背景颜色
			hSSFCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();

			hSSFFont.setColor(IndexedColors.WHITE.getIndex());
			hSSFFont.setBold(true);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle ItemStyleRed() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// 背景颜色
			hSSFCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

			HSSFFont hSSFFont = mWorkbook.createFont();

			hSSFFont.setColor(IndexedColors.WHITE.getIndex());
			hSSFFont.setBold(true);
			hSSFCellStyle.setFont((Font) hSSFFont);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	private static CellStyle ItemStyle() {
		HSSFCellStyle hSSFCellStyle = mWorkbook.createCellStyle();
		try {
			hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
			hSSFCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			hSSFCellStyle.setWrapText(true);
			hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
			hSSFCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderTop(BorderStyle.THIN);
			hSSFCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
			hSSFCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hSSFCellStyle.setBorderRight(BorderStyle.THIN);
			hSSFCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return (CellStyle) hSSFCellStyle;
	}

	/**
	 * 检查报告书写
	 * 
	 * @param wCheckList
	 * @param wFileOutputStream
	 * @param wOrder
	 */
	public static void OMS_WriteCheckReport(List<OMSCheckMsg> wCheckList, FileOutputStream wFileOutputStream,
			OMSOrder wOrder) {
		try {
			mWorkbook = new HSSFWorkbook();

			mTitleStyle = TitleStyle();
			mHeadStyle = HeadStyle();
			mItemStyle = ItemStyle();

			String[] wHeaders = { "序号", "检验类型", "提示内容" };
			// ①建Sheet
			mSheet = (Sheet) mWorkbook.createSheet(StringUtils.Format("检查报告", wOrder.PartNo));
			// ②建标题
			CellRangeAddress wRegion = new CellRangeAddress(0, 0, 0, wHeaders.length - 1);
			mSheet.addMergedRegion(wRegion);
			Row wRow = mSheet.createRow(0);
			wRow.setHeight((short) 500);
			Cell wCell = wRow.createCell(0);
			wCell.setCellValue(StringUtils.Format("{0}基础数据检查报告", wOrder.PartNo));
			wCell.setCellStyle(mTitleStyle);
			// ③建表头
			wRow = mSheet.createRow(1);
			wRow.setHeight((short) 550);
			for (int i = 0; i < wHeaders.length; i++) {
				wCell = wRow.createCell(i);
				wCell.setCellValue(wHeaders[i]);
				wCell.setCellStyle(mHeadStyle);
			}
			// ④建内容
			for (int i = 0; i < wCheckList.size(); i++) {
				wRow = mSheet.createRow(1);
				// ①序号
				wCell = wRow.createCell(0);
				wCell.setCellValue(i + 1);
				wCell.setCellStyle(mItemStyle);
				// ②检验类型
				wCell = wRow.createCell(1);
				wCell.setCellValue(wCheckList.get(i).TypeText);
				wCell.setCellStyle(mItemStyle);
				// ③提示内容
				wCell = wRow.createCell(2);
				wCell.setCellValue(wCheckList.get(i).Message);
				wCell.setCellStyle(mItemStyle);
			}
			// 导出
			Export(wFileOutputStream);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 绘制滚动计划Excel
	 */
	public static void APS_WriteScrollPlan(List<List<String>> wSourceList, FileOutputStream wFileOutputStream,
			String wType) {
		try {
			if (wSourceList == null || wSourceList.size() <= 0) {
				return;
			}

			mWorkbook = new HSSFWorkbook();

			mTitleStyle = TitleStyle();
			mHeadStyle = HeadStyle();
			mItemStyle = ItemStyle();

			// ①建Sheet
			String wTitle = StringUtils.Format("{0}年{1}滚动计划", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					wType);
			mSheet = (Sheet) mWorkbook.createSheet(wTitle);
			// ②建标题
			CellRangeAddress wRegion = new CellRangeAddress(0, 0, 0, wSourceList.get(0).size() - 1);
			mSheet.addMergedRegion(wRegion);
			Row wRow = mSheet.createRow(0);
			wRow.setHeight((short) 500);
			Cell wCell = wRow.createCell(0);
			wCell.setCellValue(wTitle);
			wCell.setCellStyle(mTitleStyle);
			// ③建表头
			wRow = mSheet.createRow(1);
			wRow.setHeight((short) 550);
			for (int i = 0; i < wSourceList.get(0).size(); i++) {
				wCell = wRow.createCell(i);
				wCell.setCellValue(wSourceList.get(0).get(i));
				wCell.setCellStyle(mHeadStyle);
			}
			// ④建内容
			int wRowNum = 2;
			for (int i = 1; i < wSourceList.size(); i++) {
				wRow = mSheet.createRow(wRowNum++);
				for (int j = 0; j < wSourceList.get(i).size(); j++) {
					wCell = wRow.createCell(j);
					wCell.setCellValue(wSourceList.get(i).get(j));
					wCell.setCellStyle(mItemStyle);
				}
			}
			// 导出
			Export(wFileOutputStream);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 导出Excel数据，通用函数
	 */
	public static String ExportData(List<MyExcelSheet> wMyExcelSheetList, String wShortFileName) {
		String wResult = "";
		try {
			if (wMyExcelSheetList == null || wMyExcelSheetList.size() <= 0) {
				return wResult;
			}

			SimpleDateFormat wSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String wCurTime = wSimpleDateFormat.format(Calendar.getInstance().getTime());

			String wFileName = StringUtils.Format("{1}{0}.xls", wCurTime, wShortFileName);
			String wDirePath = StringUtils.Format("{0}static/export/",
					Constants.getConfigPath().replace("config/", ""));

			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}

			String wFilePath = StringUtils.Format("{0}{1}", new Object[] { wDirePath, wFileName });
			File wNewFile = new File(wFilePath);
			wNewFile.createNewFile();

			FileOutputStream wFileOutputStream = new FileOutputStream(wNewFile);

			mWorkbook = new HSSFWorkbook();

			mTitleStyle = TitleStyle();
			mHeadStyle = HeadStyle();
			mItemStyle = ItemStyle();

			for (MyExcelSheet wMyExcelSheet : wMyExcelSheetList) {
				CreateSheet(wMyExcelSheet);
			}

			Export(wFileOutputStream);

			wResult = StringUtils.Format("/{0}/export/{1}",
					Configuration.readConfigString("project.name", "application"), wFileName);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建一张表格数据
	 */
	private static void CreateSheet(MyExcelSheet wMyExcelSheet) {
		try {
			mSheet = (Sheet) mWorkbook.createSheet(wMyExcelSheet.SheetName);

			int wRowIndex = 0;
			Row wRow = null;
			Cell wCell = null;

			// 设置列宽
			SetColumnWidth(wMyExcelSheet.HeaderList.size(), 7114);

			if (StringUtils.isEmpty(wMyExcelSheet.TitleName)) {

			} else {
				CellRangeAddress wRegion = new CellRangeAddress(0, 0, 0, wMyExcelSheet.HeaderList.size() - 1);
				mSheet.addMergedRegion(wRegion);

				wRow = mSheet.createRow(wRowIndex);
				wRow.setHeight((short) 500);
				wCell = wRow.createCell(0);
				wCell.setCellValue(wMyExcelSheet.TitleName);
				wCell.setCellStyle(mTitleStyle);

				wRowIndex++;
			}

			wRow = mSheet.createRow(wRowIndex);
			wRow.setHeight((short) 550);
			for (int j = 0; j < wMyExcelSheet.HeaderList.size(); j++) {
				wCell = wRow.createCell(j);
				wCell.setCellValue(wMyExcelSheet.HeaderList.get(j));
				wCell.setCellStyle(mHeadStyle);
			}
			wRowIndex++;

			for (List<String> wValueList : wMyExcelSheet.DataList) {
				WriteRowItem(wValueList, wRowIndex++);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 设置Excel列的宽度适应A4纸张
	 */
	private static void SetColumnWidth(int wLength, int wWidth) {
		try {
			for (int i = 0; i < wLength; i++) {
				mSheet.setColumnWidth(i, wWidth);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 输出Excel行数据
	 */
	public static void WriteRowItem(List<String> wValueList, int wRowNum) {
		try {
			Row wRow = mSheet.createRow(wRowNum);
//			wRow.setHeight((short) 375);

			for (int i = 0; i < wValueList.size(); i++) {
				if (wValueList.get(i).contains("http")
						&& (wValueList.get(i).contains(".jpg") || wValueList.get(i).contains(".png"))) {
					Cell wCell = wRow.createCell(i);
					wCell.setCellValue("图片");
					HSSFHyperlink wLink = (HSSFHyperlink) mCreateHelper.createHyperlink(HyperlinkType.URL);
					wLink.setAddress(wValueList.get(i));
					wCell.setHyperlink(wLink);
					wCell.setCellStyle(mLinkStyle);
				} else {
					Cell wCell = wRow.createCell(i);
					wCell.setCellValue(wValueList.get(i));
					wCell.setCellStyle(mItemStyle);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 导出生产计划
	 */
	public static String ExportProductPlan(BMSEmployee wLoginUser, List<APSTaskPart> wTaskPartList,
			List<APSTaskStepPlan> wStepPlanList, List<Integer> wMainPartIDList, List<Integer> wPartPartIDList,
			List<Integer> wDrivePartIDList, List<Integer> wTransformerPartIDList, List<OMSOrder> wOrderList) {
		String wResult = "";
		try {
			SimpleDateFormat wSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String wCurTime = wSimpleDateFormat.format(Calendar.getInstance().getTime());

			String wFileName = StringUtils.Format("{1}{0}.xls", wCurTime, "生产计划");
			String wDirePath = StringUtils.Format("{0}static/export/",
					Constants.getConfigPath().replace("config/", ""));

			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}

			String wFilePath = StringUtils.Format("{0}{1}", new Object[] { wDirePath, wFileName });
			File wNewFile = new File(wFilePath);
			wNewFile.createNewFile();

			FileOutputStream wFileOutputStream = new FileOutputStream(wNewFile);

			mWorkbook = new HSSFWorkbook();

			mTitleStyle = TitleStyle();
			mHeadStyle = HeadStyle();
			mItemStyle = ItemStyle();
			mHeadStyle_Copy = HeadStyleCopy();
			mItemStyle_Green = ItemStyleGreen();
			mItemStyle_Blue = ItemStyleBlue();
			mItemStyle_Red = ItemStyleRed();

			// ①主计划Sheet
			String wTitle = "主计划";
			CreateMainPlanSheet(wTaskPartList, wOrderList, wMainPartIDList, wTitle);
			// ②部件Sheet
			wTitle = "部件";
			CreateOtherSheet(wStepPlanList, wOrderList, wPartPartIDList, wTitle);
			// ③驱动Sheet
			wTitle = "驱动";
			CreateOtherSheet(wStepPlanList, wOrderList, wDrivePartIDList, wTitle);
			// ④变压器Sheet
			wTitle = "变压器";
			CreateOtherSheet(wStepPlanList, wOrderList, wTransformerPartIDList, wTitle);

			Export(wFileOutputStream);

			wResult = StringUtils.Format("/{0}/export/{1}",
					Configuration.readConfigString("project.name", "application"), wFileName);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 创建其他部件、驱动计划
	 */
	private static void CreateOtherSheet(List<APSTaskStepPlan> wStepPlanList, List<OMSOrder> wOrderList,
			List<Integer> wPartIDList, String wTitle) {
		try {
			mSheet = (Sheet) mWorkbook.createSheet(wTitle);

			int wRowIndex = 0;
			Row wRow = null;
			Cell wCell = null;

			for (int wPartID : wPartIDList) {

				List<Integer> wStepList = wStepPlanList.stream().filter(p -> p.PartID == wPartID).map(p -> p.StepID)
						.distinct().collect(Collectors.toList());

				// 设置列宽
				SetColumnWidth(5 + wStepList.size(), 7114);

				if (StringUtils.isEmpty(wTitle)) {

				} else {
					String wPartName = "";
					if (wStepPlanList.stream().anyMatch(p -> p.PartID == wPartID)) {
						wPartName = wStepPlanList.stream().filter(p -> p.PartID == wPartID).findFirst().get().PartName;
					}

					CellRangeAddress wRegion = new CellRangeAddress(wRowIndex, wRowIndex, 0, 5 + wStepList.size() - 1);
					mSheet.addMergedRegion(wRegion);

					wRow = mSheet.createRow(wRowIndex);
					wRow.setHeight((short) 500);
					wCell = wRow.createCell(0);
					wCell.setCellValue(wPartName);
					wCell.setCellStyle(mTitleStyle);

					wRowIndex++;
				}

				List<String> wHearderList = new ArrayList<String>(Arrays.asList("序号", "局段", "修程", "车型", "车号"));
				// 左半部分
				wRow = mSheet.createRow(wRowIndex);
				wRow.setHeight((short) 550);
				for (int j = 0; j < wHearderList.size(); j++) {
					wCell = wRow.createCell(j);
					wCell.setCellValue(wHearderList.get(j));
					wCell.setCellStyle(mHeadStyle_Copy);
				}
				// 右半部分
				List<String> wRightList = new ArrayList<String>();
				for (int wStepID : wStepList) {
					if (wStepPlanList.stream().anyMatch(p -> p.PartID == wPartID && p.StepID == wStepID)) {
						String wStepName = wStepPlanList.stream()
								.filter(p -> p.PartID == wPartID && p.StepID == wStepID).findFirst().get().StepName;
						wRightList.add(wStepName);
					}
				}
				for (int i = 0; i < wRightList.size(); i++) {
					wCell = wRow.createCell(i * 2 + 5);
					wCell.setCellValue(wRightList.get(i));
					wCell.setCellStyle(mHeadStyle_Copy);

					// 合并单元格
					CellRangeAddress wRegion = new CellRangeAddress(wRowIndex, wRowIndex, i * 2 + 5, i * 2 + 6);
					mSheet.addMergedRegion(wRegion);
				}

				wRowIndex++;

				List<List<ExcelColorCol>> wDataList = GetOtherDataList(wStepPlanList, wStepList, wPartID, wOrderList);

				for (List<ExcelColorCol> wValueList : wDataList) {
					WriteColorRowItem(wValueList, wRowIndex++);
				}

				wRowIndex++;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取其他部件、驱动等行数据
	 */
	private static List<List<ExcelColorCol>> GetOtherDataList(List<APSTaskStepPlan> wStepPlanList,
			List<Integer> wStepList, int wPartID, List<OMSOrder> wOrderList) {
		List<List<ExcelColorCol>> wResult = new ArrayList<List<ExcelColorCol>>();
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			SimpleDateFormat wSDF = new SimpleDateFormat("MM-dd");

			int wIndex = 1;
			for (OMSOrder wOMSOrder : wOrderList) {

				List<ExcelColorCol> wList = new ArrayList<ExcelColorCol>();

				List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex),
						wOMSOrder.Customer, wOMSOrder.LineName, wOMSOrder.ProductNo, wOMSOrder.PartNo.split("#")[1]));

				for (String wValue : wValueList) {
					ExcelColorCol wCol = new ExcelColorCol(wValue, 0);
					wList.add(wCol);
				}

				for (int wStepiD : wStepList) {
					if (wStepPlanList.stream()
							.anyMatch(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID && p.StepID == wStepiD)) {
						APSTaskStepPlan apsTaskStepPlan = wStepPlanList.stream()
								.filter(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID && p.StepID == wStepiD)
								.findFirst().get();

						int wColor = GetColor(apsTaskStepPlan);

						apsTaskStepPlan.EndTime.add(Calendar.HOUR_OF_DAY, -12);
						String wPlan = StringUtils.Format("{0}(计划)", wSDF.format(apsTaskStepPlan.EndTime.getTime()));

						ExcelColorCol wCol = new ExcelColorCol(wPlan, 0);
						wList.add(wCol);

						String wReal = StringUtils.Format("{0}(实际)",
								wSDF.format(apsTaskStepPlan.RealEndTime.getTime()));

						if (apsTaskStepPlan.RealEndTime.compareTo(wBaseTime) <= 0) {
							wReal = "";
						}

						wCol = new ExcelColorCol(wReal, wColor);
						wList.add(wCol);
					} else {
						ExcelColorCol wCol = new ExcelColorCol("", 0);
						wList.add(wCol);

						wCol = new ExcelColorCol("", 0);
						wList.add(wCol);
					}
				}

				wResult.add(wList);

				wIndex++;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	private static int GetColor(APSTaskStepPlan apsTaskStepPlan) {
		int wResult = 0;
		try {
			if (apsTaskStepPlan.Status == 5) {
				wResult = 5;
			} else {
				SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
				int wRShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
				Calendar wCloneEndTime = (Calendar) apsTaskStepPlan.EndTime.clone();
				wCloneEndTime.add(Calendar.HOUR_OF_DAY, -12);

				int wPShiftID = Integer.parseInt(wSDF.format(wCloneEndTime.getTime()));
				int wSShiftID = Integer.parseInt(wSDF.format(apsTaskStepPlan.StartTime.getTime()));

				if (wRShiftID > wPShiftID) {
					wResult = 6;
				} else {
					if (apsTaskStepPlan.Status == 2 && wRShiftID >= wSShiftID) {
						wResult = 4;
					} else {
						wResult = apsTaskStepPlan.getStatus();
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建主计划
	 */
	private static void CreateMainPlanSheet(List<APSTaskPart> wTaskPartList, List<OMSOrder> wOrderList,
			List<Integer> wMainPartIDList, String wTitle) {
		try {
			mSheet = (Sheet) mWorkbook.createSheet(wTitle);

			int wRowIndex = 0;
			Row wRow = null;
			Cell wCell = null;

			// 设置列宽
			SetColumnWidth(6 + wMainPartIDList.size(), 7114);

			if (StringUtils.isEmpty(wTitle)) {
			} else {
				CellRangeAddress wRegion = new CellRangeAddress(0, 0, 0, 6 + wMainPartIDList.size() - 1);
				mSheet.addMergedRegion(wRegion);

				wRow = mSheet.createRow(wRowIndex);
				wRow.setHeight((short) 500);
				wCell = wRow.createCell(0);
				wCell.setCellValue(wTitle);
				wCell.setCellStyle(mTitleStyle);

				wRowIndex++;
			}

			List<String> wHearderList = new ArrayList<String>(Arrays.asList("序号", "局段", "修程", "车型", "车号", "检修停时"));
			wRow = mSheet.createRow(wRowIndex);
			wRow.setHeight((short) 550);
			// 左半部分
			for (int j = 0; j < wHearderList.size(); j++) {
				wCell = wRow.createCell(j);
				wCell.setCellValue(wHearderList.get(j));
				wCell.setCellStyle(mHeadStyle_Copy);
			}
			// 右半部分
			List<String> wRightList = new ArrayList<String>();
			for (int wPartID : wMainPartIDList) {
				if (wTaskPartList.stream().anyMatch(p -> p.PartID == wPartID)) {
					String wPartName = wTaskPartList.stream().filter(p -> p.PartID == wPartID).findFirst()
							.get().PartName;
					wRightList.add(wPartName);
				}
			}

			for (int i = 0; i < wRightList.size(); i++) {
				wCell = wRow.createCell(i * 2 + 6);
				wCell.setCellValue(wRightList.get(i));
				wCell.setCellStyle(mHeadStyle_Copy);

				// 合并单元格
				CellRangeAddress wRegion = new CellRangeAddress(wRowIndex, wRowIndex, i * 2 + 6, i * 2 + 7);
				mSheet.addMergedRegion(wRegion);
			}

			wRowIndex++;

			List<List<ExcelColorCol>> wDataList = GetMainDataList(wTaskPartList, wMainPartIDList, wOrderList);

			for (List<ExcelColorCol> wValueList : wDataList) {
				WriteColorRowItem(wValueList, wRowIndex++);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 绘制有颜色的单元格
	 */
	private static void WriteColorRowItem(List<ExcelColorCol> wValueList, int wRowNum) {
		try {
			try {
				Row wRow = mSheet.createRow(wRowNum);

				for (int i = 0; i < wValueList.size(); i++) {
					Cell wCell = wRow.createCell(i);
					wCell.setCellValue(wValueList.get(i).Value);

					switch (wValueList.get(i).Color) {
					case 4:
						wCell.setCellStyle(mItemStyle_Blue);
						break;
					case 5:
						wCell.setCellStyle(mItemStyle_Green);
						break;
					case 6:
						wCell.setCellStyle(mItemStyle_Red);
						break;
					default:
						wCell.setCellStyle(mItemStyle);
						break;
					}
				}
			} catch (Exception ex) {
				logger.error(ex.toString());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取主计划行数据
	 */
	private static List<List<ExcelColorCol>> GetMainDataList(List<APSTaskPart> wTaskPartList,
			List<Integer> wMainPartIDList, List<OMSOrder> wOrderList) {
		List<List<ExcelColorCol>> wResult = new ArrayList<List<ExcelColorCol>>();
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			SimpleDateFormat wSDF = new SimpleDateFormat("MM-dd");

			int wIndex = 1;
			for (OMSOrder wOMSOrder : wOrderList) {

				int dayInterval = CalendarUtil.getDayInterval(wOMSOrder.RealReceiveDate.getTime(),
						Calendar.getInstance().getTime());

				List<ExcelColorCol> wList = new ArrayList<ExcelColorCol>();

				List<String> wValueList = new ArrayList<String>(
						Arrays.asList(String.valueOf(wIndex), wOMSOrder.Customer, wOMSOrder.LineName,
								wOMSOrder.ProductNo, wOMSOrder.PartNo.split("#")[1], String.valueOf(dayInterval)));

				for (String wValue : wValueList) {
					ExcelColorCol wCol = new ExcelColorCol(wValue, 0);
					wList.add(wCol);
				}

				for (int wPartID : wMainPartIDList) {
					if (wTaskPartList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID)) {
						APSTaskPart apsTaskPart = wTaskPartList.stream()
								.filter(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID).findFirst().get();

						int wColor = GetColor(apsTaskPart);

						apsTaskPart.EndTime.add(Calendar.HOUR_OF_DAY, -12);
						String wPlan = StringUtils.Format("{0}(计划)", wSDF.format(apsTaskPart.EndTime.getTime()));

						ExcelColorCol wCol = new ExcelColorCol(wPlan, 0);
						wList.add(wCol);

						String wReal = StringUtils.Format("{0}(实际)", wSDF.format(apsTaskPart.FinishWorkTime.getTime()));

						if (apsTaskPart.FinishWorkTime.compareTo(wBaseTime) <= 0) {
							wReal = "";
						}

						wCol = new ExcelColorCol(wReal, wColor);
						wList.add(wCol);
					} else {
						ExcelColorCol wCol = new ExcelColorCol("", 0);
						wList.add(wCol);

						wCol = new ExcelColorCol("", 0);
						wList.add(wCol);
					}
				}

				wResult.add(wList);

				wIndex++;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取颜色 5绿色，4蓝色，6红色，其他无色
	 */
	private static int GetColor(APSTaskPart apsTaskPart) {
		int wResult = 0;
		try {
			if (apsTaskPart.Status == 5) {
				wResult = 5;
			} else {
				SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
				int wRShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
				Calendar wCloneEndTime = (Calendar) apsTaskPart.EndTime.clone();
				wCloneEndTime.add(Calendar.HOUR_OF_DAY, -12);

				int wPShiftID = Integer.parseInt(wSDF.format(wCloneEndTime.getTime()));
				int wSShiftID = Integer.parseInt(wSDF.format(apsTaskPart.StartTime.getTime()));

				if (wRShiftID > wPShiftID) {
					wResult = 6;
				} else {
					if (apsTaskPart.Status == 2 && wRShiftID >= wSShiftID) {
						wResult = 4;
					} else {
						wResult = apsTaskPart.getStatus();
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}