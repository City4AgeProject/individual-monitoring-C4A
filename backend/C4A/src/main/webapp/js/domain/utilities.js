
function shortenText(text, newlength) {
    return text.substr(0, text.length>=newlength ? newlength : text.length);
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
