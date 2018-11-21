logger.log(args.titulo);
if (args.titulo != null && args.titulo != "{titulo}" && args.titulo!="") {

	var nodes = search.luceneSearch("@doc\\:titulo:" + args.titulo);
	model.resultset = nodes;

}
