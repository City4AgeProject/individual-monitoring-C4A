
function shortenText(text, newlength) {
    if(text)
        return text.substr(0, text.length>=newlength ? newlength : text.length);
    else
        return '';
}

remove_item = function (arr, value) {
    var b = '';
    for (b in arr) {
        if (arr[b] === value) {
            arr.splice(b, 1);
            break;
        }
    }
    return arr;
}
