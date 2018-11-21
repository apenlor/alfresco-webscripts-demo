/* Log */
var milliseconds = new Date().getTime();
var logFile = userhome.childByNamePath("delete_node_" + milliseconds + ".log");
if (logFile == null)
{
    logFile = userhome.createFile("delete_node" + milliseconds + ".log");
}


/* Outputs */
var resultString = "Action failed";
var resultCode = false;

var node = null;

var nodeId = args.nodeid;
if ((nodeId != "") && (nodeId != null))
{
    var node = search.findNode("workspace://SpacesStore/" + nodeId);
}

if ((args.storeid) && (args.storeid != "") && (args.path) && (args.path != ""))
{
    var store = avm.lookupStore(args.storeid);
    if (store != null)
    {
        var pathWithStore = args.storeid + ":" + args.path;
        var node = avm.lookupNode(pathWithStore);
    }
}


if (node != null)
{
    try
    {
        resultString = "Could not delete document/folder";
        if (node.remove())
        {
            resultString = "Document/Folder deleted";
            resultCode = true;
        }
    } catch (e)
    {
        resultString = "Action failed due to exception";
    }
    logFile.content = resultString + ": " + node;
} else {
    logFile.content = resultString + ": " + nodeId;
}



model.resultString = resultString;
model.resultCode = resultCode;



