package com.snapdeal.scm.core.utils;

import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.ElasticScriptKey;

import java.util.HashMap;
import java.util.Map;

/**
 * @author prateek
 */
public class ElasticScriptsUtil {

    private static Map<ElasticScriptKey, String> elasticScripts = new HashMap<>();

    static {
        elasticScripts.put(ElasticScriptKey.ORDER_TO_DELIVERED, createDateSubtractScript(SubOrderDetailElasticColumn.ORDER_CREATED_DATE, SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE,
                                                                                         SubOrderDetailElasticColumn.ORDER_TO_DELIVERED));
        elasticScripts.put(ElasticScriptKey.ORDER_TO_SHIPPED, createDateSubtractScript(SubOrderDetailElasticColumn.ORDER_CREATED_DATE, SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE,
                                                                                       SubOrderDetailElasticColumn.ORDER_TO_SHIPPED));
        elasticScripts.put(ElasticScriptKey.SHIPPED_TO_DELIVERED, createDateSubtractScript(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE, SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE,
                                                                                           SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED));
        elasticScripts.put(ElasticScriptKey.ATTEMPTED, getAttemptedScript());
        elasticScripts.put(ElasticScriptKey.UDL, getUDLStatus());
        elasticScripts.put(ElasticScriptKey.RTO_DATE, setRTODateScript());
        elasticScripts.put(ElasticScriptKey.CLOSED_DATE, setClosedDateScript());
        elasticScripts.put(ElasticScriptKey.RAD, setRADDateScript());
        elasticScripts.put(ElasticScriptKey.CONNECTED, setConnectedDateScript());
        elasticScripts.put(ElasticScriptKey.LOST_AND_DAMAGED_DATE, setLostAndDamagedDateScript());
        elasticScripts.put(ElasticScriptKey.OFD, setOFDDateScript());
    }

    public static String getElasticScript(ElasticScriptKey elasticScriptKey) {
        return elasticScripts.get(elasticScriptKey);
    }

    private static String setConnectedDateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.TP_CONNECTED_STATUS_DATE.getColumnName());
    }

    private static String setRADDateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.TP_RAD_STATUS_DATE.getColumnName());
    }

    private static String getFirstScanValueScript(String columnName) {
        StringBuilder script = new StringBuilder();
        script.append("if(ctx._source.containsKey('").append(columnName)
                .append("'){date = Date.parse(\"yyyy-MM-dd\\'T\\'HH:mm:ss\", ctx._source.")
                .append(columnName)
                .append(")if(")
                .append(columnName)
                .append(".before(date)){ctx._source.")
                .append(columnName)
                .append(" = ")
                .append(columnName)
                .append(";};};else{ctx._source.")
                .append(columnName)
                .append(" = ")
                .append(columnName)
                .append(";}");
        return script.toString();
    }

    private static String createDateSubtractScript(SubOrderDetailElasticColumn subtractFrom, SubOrderDetailElasticColumn subtractTo, SubOrderDetailElasticColumn output) {
        StringBuilder script = new StringBuilder();
        script.append("if( ctx._source.containsKey('").append(subtractFrom.getColumnName())
                .append("') && ctx._source.containsKey('").append(subtractTo.getColumnName())
                .append("') ){sdf = new java.text.SimpleDateFormat('yyyy-MM-dd\\'T\\'HH:mm:ss');startDate = sdf.parse(ctx._source.").append(subtractFrom.getColumnName())
                .append(");endDate = sdf.parse(ctx._source.").append(subtractTo.getColumnName())
                .append("); def duration = groovy.time.TimeCategory.minus(endDate,startDate); ctx._source.")
                .append(output.getColumnName()).append(" = ((duration.days * 24) + duration.hours);}");
        return script.toString();
    }

    private static String getAttemptedScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName());

    }

    private static String setRTODateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.RTO_DATE.getColumnName());
    }

    private static String setClosedDateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
    }

    private static String setLostAndDamagedDateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.LOST_AND_DAMAGED_DATE.getColumnName());
    }

    private static String setOFDDateScript() {
        return getFirstScanValueScript(SubOrderDetailElasticColumn.TP_OFD_STATUS_DATE.getColumnName());
    }

/*	private static String getUDLStatus() {
        String columnName = SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName();
		StringBuilder script = new StringBuilder();
		script.append("newDateSDF= Date.parse('yyyy-MM-dd', ")
				.append(columnName)
				.append(".format('yyyy-MM-dd'));if(!ctx._source.")
				.append(columnName)
				.append("){ctx._source.")
				.append(columnName)
				.append(" = ")
				.append(columnName)
				.append(";};else if(!ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_SECOND_UDL_STATUS_DATE.getColumnName())
				.append("){if(groovy.time.TimeCategory.minus(newDateSDF,Date.parse('yyyy-MM-dd', Date.parse(\"yyyy-MM-dd\\'T\\'HH:mm:ss\",ctx._source.")
				.append(columnName)
				.append(").format('yyyy-MM-dd'))).getDays() > 0){ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_SECOND_UDL_STATUS_DATE.getColumnName())
				.append("=")
				.append(columnName)
				.append(";}};else if(!ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_THIRD_UDL_STATUS_DATE.getColumnName())
				.append("){if(groovy.time.TimeCategory.minus(newDateSDF,Date.parse('yyyy-MM-dd', Date.parse(\"yyyy-MM-dd\\'T\\'HH:mm:ss\",ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_SECOND_UDL_STATUS_DATE.getColumnName())
				.append(").format('yyyy-MM-dd'))).getDays() > 0){ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_THIRD_UDL_STATUS_DATE.getColumnName())
				.append("=")
				.append(columnName)
				.append(";}};else if(!ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_FOURTH_UDL_STATUS_DATE.getColumnName())
				.append("){if(groovy.time.TimeCategory.minus(newDateSDF,Date.parse('yyyy-MM-dd', Date.parse(\"yyyy-MM-dd\\'T\\'HH:mm:ss\",ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_THIRD_UDL_STATUS_DATE.getColumnName())
				.append(").format('yyyy-MM-dd'))).getDays() > 0){ctx._source.")
				.append(SubOrderDetailElasticColumn.TP_FOURTH_UDL_STATUS_DATE.getColumnName())
				.append("=")
				.append(columnName)
				.append(";}}");
		return script.toString();
	}*/

    private static String getUDLStatus() {
        String        columnPrefix = "tp_udl_status_date_";
        StringBuilder script       = new StringBuilder();
        script.append("j=1;for(i=1; i<=4; i++){if(ctx._source.")
                .append(columnPrefix)
                .append(" + i){if(Date.parse('yyyy-MM-dd', ctx._source.")
                .append(columnPrefix)
                .append("+i.format('yyyy-MM-dd'))== Date.parse('yyyy-MM-dd', Date.parse(\"yyyy-MM-dd\\'T\\'HH:mm:ss\",")
                .append(columnPrefix)
                .append("+j).format('yyyy-MM-dd'))){j++;}};else{if(")
                .append(columnPrefix)
                .append("+j){ctx._source.")
                .append(columnPrefix)
                .append("+i=")
                .append(columnPrefix)
                .append("+j;}}}");

        return script.toString();
    }
}