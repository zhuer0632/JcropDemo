// 把多行数据，绑定到一个表格上
function bindTrInTable(tbObj, trStr) {
    $(tbObj).children().find("tr").last().after(trStr);
}

// 清空表格中的所有行
function clearTableTr(tbObj) {
    $(tbObj).children().find("tr").each(function (i, element) {
        if (i > 0) {
            $(element).remove();
        }
    });
}


/**
* ajax提交数据[data_v:getPostDatas($(document))]
* 
* @param {}
*            data_v
* @param {}
*            action
*/
function postForm(data_v, action) {
    var out;
    $.ajax({
        async: false,
        url: action,
        data: data_v,
        type: "post",
        success: function (result) {
            out = result;
        }

    });
    return out;
}

/**
 * 跟上面的函数没有区别;仅仅为了方法名的见名知意[data_v: a=xxx&b=yyyy]
 * @param {} data_v
 * @param {} action
 * @return {}
 */
function getJson(data_v, action) {
    var out;
    $.ajax({
        async: false,
        url: action,
        data: data_v,
        type: "post",
        success: function (result) {
            out = result;
        }

    });
    return out;
}


// 取得一个页面上的数据
// 调用示例:getPostDatas($(document));
function getPostDatas(win) {

    var data_v = new Object();

    // 搜集参数 [肯定就两种形式一种是input,一种是select]

    $(win).find("input").each(function (index, element) {
        // checkbox ck1=true 或者 ck1=true,false
        if ($(element).attr("name") && $(element).attr("type") == "checkbox") {
            if (data_v[$(element).attr("name")] != undefined) {
                data_v[$(element).attr("name")] = data_v[$(element)
						.attr("name")]
						+ "," + $(element).attr("checked");
            } else {
                data_v[$(element).attr("name")] = $(element).attr("checked");
            }
            return;
        }
        // radio : rabtn1="v1" 或者 rabtn1=""
        if ($(element).attr("name") && $(element).attr("type") == "radio") {
            // 如果已经设置过值,直接退出
            if (data_v[$(element).attr("name")] != undefined
					&& data_v[$(element).attr("name")] != "") {
                return;
            }
            if ($(element).attr("checked")) {
                data_v[$(element).attr("name")] = $(element).attr("value");
                return;
            } else {
                data_v[$(element).attr("name")] = "";
                return;
            }
        }

        // text hidden
        if ($(element).attr("name")) {

            if (data_v[$(element).attr("name")] != undefined) {
                data_v[$(element).attr("name")] = data_v[$(element)
						.attr("name")]
						+ "," + $(element).val();

            } else {
                data_v[$(element).attr("name")] = $(element).val();
            }
        }
    });

    $(win).find("select").each(function (index, element) {
        if ($(element).attr("name") != undefined) {
            if (data_v[$(element).attr("name")]) {
                data_v[$(element).attr("name")] = data_v[$(element)
						.attr("name")]
						+ "," + $(element).val();
            } else {
                data_v[$(element).attr("name")] = $(element).val();
            }
        }
    });

    // textarea
    $(win).find("textarea").each(function (index, element) {
        if ($(element).attr("name") != undefined) {
            if (data_v[$(element).attr("name")]) {
                data_v[$(element).attr("name")] = data_v[$(element)
						.attr("name")]
						+ "," + $(element).val();
            } else {
                data_v[$(element).attr("name")] = $(element).val();
            }
        }
    });

    return data_v;
}


