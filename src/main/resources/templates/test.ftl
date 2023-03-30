<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"></meta>
    <title>
        ${title!""}
    </title>
    <style>
        body {
            font-family: SimSun;
        }
    </style>
</head>
<body>
<h2 style="text-align: center">${title!""}</h2>
<p style="text-indent: 2em">信息</p>
<br/>
<br/>
<p style="font-weight: bold">基本信息:</p>
<p style="text-indent: 2em">用户名称:${name!""}</p>
<br/>
<br/>
<p style="font-weight: bold">集合:</p>
<table border="1" cellspacing="0" width="100%">
    <tr bgcolor="#6495ed" style="font-weight: bold">
        <td width="10%">字段1</td>
        <td width="15%">字段2</td>
        <td width="10%">字段3</td>
        <td width="25%">字段4</td>
        <td width="20%">字段5</td>
        <td width="10%">字段6</td>
        <td width="10%">字段7</td>
    </tr>
    <#if reportList?? && (reportList?size>0)>
        <#list reportList as reportData>
            <#if reportData??>
                <#if reportData_index == 0>
                    <tr style="page-break-inside: avoid;page-break-after: auto;">
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column1!""}</td>
                        <td width="15%" style="word-break: break-all;word-wrap: break-word">${reportData.column2!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column3!""}</td>
                        <td width="25%" style="word-break: break-all;word-wrap: break-word">${reportData.column4!""}</td>
                        <td width="20%" style="word-break: break-all;word-wrap: break-word">${reportData.column5!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column6!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column7!""}</td>
                    </tr>
                <#else >
                    <#assign preData=reportList[reportData_index-1] />
                    <tr style="page-break-inside: avoid;page-break-after: auto;">
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column1!""}</td>
                        <td width="15%" style="word-break: break-all;word-wrap: break-word">${reportData.column2!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column3!""}</td>
                        <td width="25%" style="word-break: break-all;word-wrap: break-word">${reportData.column4!""}</td>
                        <td width="20%" style="word-break: break-all;word-wrap: break-word">${reportData.column5!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column6!""}</td>
                        <td width="10%" style="word-break: break-all;word-wrap: break-word">${reportData.column7!""}</td>
                    </tr>
                </#if>
            </#if>
        </#list>
    </#if>
</table>
<br/>
<br/>
</body>
</html>