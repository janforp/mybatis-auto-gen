<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

    <style type="text/css">
        td {
            border-style: solid;
            border-width: 0;
            padding: 2px;
        }

        .template-title {
            text-align: center;
            font-weight: bold;
            font-size: 20px;
            margin: 20px 0 10px 0;
        }

        .template-tab {
            width: 100%;
        }

        .template-tab > div {
            width: 33%;
            display: inline-block;
        }

        .template-table {
            width: 100%;
            clear: both;
            border-left: 1px solid;
            table-layout: fixed;
            word-break: break-strict;
            border-collapse: collapse;
        }

        .template-table2 {
            border-top: 1px solid;
        }

        td, th {
            padding: 6px 4px;
            border: 1px solid black;
            border-top: 0;
            border-left: 0;
            font-size: 9px;
        }

        .table-td {
            /* position: relative; */
        }

        .table-td-mouth {
            /* position: absolute; */
            /* right: 20px; */
            text-align: right;
            padding-right: 20px;
        }
    </style>
</head>
<body class="page-printer-a4-landscape">
<div>
    <div class="template-title">社会保险费缴费申报表（适用特殊缴费）</div>
    <div class="template-tab">
        <div>*纳税人识别号：${taxNo!""}</div>
        <div>*纳税人名称（姓名）：${taxPayerName!""}</div>
        <div>*申报性质：</div>
    </div>
    <div>
        <table class="template-table template-table2" cellspacing="0" cellpadding="0">
            <colgroup width="3%"/>
            <colgroup width="15%"/>
            <colgroup width="7%"/>
            <colgroup width="4%"/>
            <colgroup width="4%"/>
            <colgroup width="4%"/>
            <colgroup width="10%"/>
            <colgroup width="10%"/>
            <colgroup width="8%"/>
            <colgroup width="8%"/>
            <colgroup width="6%"/>
            <colgroup width="6%"/>
            <colgroup width="8%"/>
            <colgroup width="7%"/>
            <tbody>
            <tr>
                <th>*序号</th>
                <th>*社会保险经办机构</th>
                <th>单位编号</th>
                <th>人员编号</th>
                <th>证件类型</th>
                <th>证件号码</th>
                <th>*征集通知流水号</th>
                <th>*参保险种</th>
                <th>*征收品目</th>
                <th>*征收子目</th>
                <th>*费款所属期起</th>
                <th>*费款所属期止</th>
                <th>*特殊缴费类型</th>
                <th>*本期应纳费额</th>
            </tr>
            <tr>
                <td align="center">1</td>
                <td align="center">2</td>
                <td align="center">3</td>
                <td align="center">4</td>
                <td align="center">5</td>
                <td align="center">6</td>
                <td align="center">7</td>
                <td align="center">8</td>
                <td align="center">9</td>
                <td align="center">10</td>
                <td align="center">11</td>
                <td align="center">12</td>
                <td align="center">13</td>
                <td align="center">14</td>
            </tr>
            <#list detailList as detailItem>
                <tr>
                    <td align="center">${detailItem_index + 1}</td>
                    <td align="center">${detailItem.socinsAgencyName}</td>
                    <td align="center">${detailItem.socinsNumber}</td>
                    <td align="center">--</td>
                    <td align="center">--</td>
                    <td align="center">--</td>
                    <td align="center">${detailItem.imposeNoticeNumber}</td>
                    <td align="center">${detailItem.imposeProjectName}</td>
                    <td align="center">${detailItem.imposeItemName}</td>
                    <td align="center">${detailItem.imposeSubheadingName}</td>
                    <td align="center">${detailItem.periodStart}</td>
                    <td align="center">${detailItem.periodEnd}</td>
                    <td align="center">${detailItem.paymentType}</td>
                    <td align="center">${detailItem.withholdingAmount}</td>
                </tr>
            </#list>
            <tr>
                <td align="center">*合计</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">——</td>
                <td align="center">${totalWithholdingAmount}</td>
            </tr>
            </tbody>
        </table>
        <table class="template-table" cellspacing="0" cellpadding="0">
            <colgroup width="3%"/>
            <colgroup width="23%"/>
            <colgroup width="4%"/>
            <colgroup width="32%"/>
            <colgroup width="4%"/>
            <colgroup width="34%"/>
            <tbody>
            <tr>
                <td align="center">
                    <div>*<br/>缴<br/>费<br/>人<br/>申<br/>明</div>
                </td>
                <td align="top" class="table-td">
                    <div>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本单位所申报的社会保险费真实、准确并完整，与事实相符。<br/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;法定代表人（负责人）签名：
                    </div>
                    <div class="table-td-mouth">
                        年
                        &nbsp;&nbsp;&nbsp;&nbsp;月
                        &nbsp;&nbsp;&nbsp;&nbsp;日
                    </div>
                </td>
                <td align="center">*<br/>授<br/>权<br/>人<br/>申<br/>明</td>
                <td align="top" class="table-td">
                    <div>
                        &nbsp;&nbsp;&nbsp;&nbsp;我单位授权______________________________
                        为本单位代理申报人，任何与申报有关的来往文件，都可寄此代理机构。<br/><br/>

                        &nbsp;&nbsp;&nbsp;&nbsp;委托代理合同号：
                        <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;授权人：</div>
                    </div>
                    <div class="table-td-mouth">
                        年
                        &nbsp;&nbsp;&nbsp;&nbsp;月
                        &nbsp;&nbsp;&nbsp;&nbsp;日
                    </div>
                </td>
                <td align="center">*<br/>代<br/>理<br/>人<br/>申<br/>明</td>
                <td class="table-td">
                    <div>
                        &nbsp;&nbsp;&nbsp;&nbsp;本申报表是按照社会保险费有关规定填报，我确认其真实、完整并合法。<br/><br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;代理人（签章）：
                        <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;经办人：</div>
                    </div>
                    <div class="table-td-mouth">
                        年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <table class="template-table" cellspacing="0" cellpadding="0">
            <colgroup width="30%"/>
            <colgroup width="70%"/>
            <tbody>
            <tr>
                <td align="center">缴费人声明（自然人缴费人）</td>
                <td class="table-td">
                    <div>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本人已阅读相关社保费政策文件，确认以上申报信息准确无误。<br/>
                    </div>
                    <div class="table-td-mouth" style="right: 80px;">缴费人（签章）：</div>
                    <br/>
                    <div class="table-td-mouth">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <table class="template-table" cellspacing="0" cellpadding="0">
            <colgroup width="23%"/>
            <colgroup width="23%"/>
            <colgroup width="23%"/>
            <tbody>
            <tr>
                <td>*受理税务机关：</td>
                <td>*受理人：</td>
                <td>
                    <div>*受理日期：&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</div>
                </td>
                <td>备注：</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>