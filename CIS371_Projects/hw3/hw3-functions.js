/**
 * Created by Hans Dulimarta Fall 2017.
 * @author Adam Stewart
 * @version 09/22/2017
 */

/**
 * Given a node with id {rootId}, the following function finds all its descendant
 * elements having its attribute ID set. The function returns the number of
 * such elements. ALSO, for each such element this function sets its class
 * to {klazName}.
 *
 * @param rootId, klazName
 * @returns {number}
 */
function findElementsWithId(rootId, klazName) {

    var count = 0;
    var match = document.getElementById(rootId); //Finds root.
    if(match.hasChildNodes()) { //Checks if there's existing children.
        for(var i = 0; i < match.childNodes.length; i++) { //Loop through all children.
            if(match.childNodes[i].id) { //If child has ID, add className klazName.
                match.childNodes[i].className += klazName;
                count++;
            }
        }
    }

    return count;
}

/**
 * The following function finds all elements with attribute 'data-gv-row' (or
 * 'data-gv-column') and create a table of the desired dimension as a child of
 * the element.
 *
 * @returns NONE
 */
function createTable() {

    var firstRow = true; //Keeps track of info should be 'Heading N'.
    var rows = parseInt(document.getElementsByClassName("table-home")[0].getAttribute("data-gv-row"))+1; //+1 for hding.
    var cols = parseInt(document.getElementsByClassName("table-home")[0].getAttribute("data-gv-column")); //Cstm attr.

    var body = document.getElementsByClassName("table-home"); //Get desired parent.
    var tbl = document.createElement("table"); //Create table.
    var tblBody = document.createElement("tbody"); //Create table body.

    for(var i = 0; i < rows; i++) { //Cycle for every row.
        var row = document.createElement("tr"); //Create table row element.
        for (var j = 0; j < cols; j++) { //Cycle for every column.
            if(firstRow) { //If its the first row, add special headers.
                var cell = document.createElement("th"); //Create table data.
                var cellText = document.createTextNode("Heading " + (j+1));

                cell.appendChild(cellText); //Append text to cell.
                row.appendChild(cell); //Append cell to row.
            } else {
                var lipsum = new LoremIpsum(); //Otherwise, data is random lipsum word.
                var cell = document.createElement("td"); //Create table data.
                var cellText = document.createTextNode(lipsum.generate(3).slice(0, -1)); //Chop off the period on end.

                cell.appendChild(cellText); //Append text to cell.
                row.appendChild(cell); //Append cell to row.
            }
        }
        firstRow = false; //Make sure once we go through once, we're defintely not on the firstRow.
        tblBody.appendChild(row); //Append each row to the table body.
    }

    tbl.appendChild(tblBody); //Append table body to the table.
    body[0].appendChild(tbl); //Append table body to the overall body.
    tbl.setAttribute("border", "2"); //Some border.

}