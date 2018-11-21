<html>
    <head>
        <style>
            .formulario {border: 1px solid #FF6600; background : #DFE6ED ; width:500px; margin:10px; padding:10px;}
        </style>
    </head>
    <body>
        <#setting locale="en"/>
        <#if resultset??>
        <#list resultset as node>
        <table id="dialog:dialog-body:content-props" >
            <tbody>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan='2'>

                        <div class="wizardSectionHeading mainSubTitle" style="margin-top: 6px; margin-bottom: 6px;">
                            Datos del documento: <b>${node.properties["doc:titulo"]!""}</b>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan='2'>                
                        <div class="wizardSectionHeading mainSubTitle" style="margin-top: 6px; margin-bottom: 6px;">
                            NodeRef: <b>${node.nodeRef.id!""}</b>
                        </div>                
                    </td>
                </tr>            
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><span id="dialog:dialog-body:content-props:prop_fact_proveedor:label_fact_coddivisionesROSS">Resumen</span>
                    </td>
                    <td><input type="textarea" name="resumen" value='${node.properties["doc:resumen"]!""}' />
                    </td>
                </tr> 
            </tbody>
        </table>
        </#list>
        </#if>    
    </body>
</html>
