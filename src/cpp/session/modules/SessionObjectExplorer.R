#
# SessionObjectExplorer.R
#
# Copyright (C) 2009-17 by RStudio, Inc.
#
# Unless you have received this program directly from RStudio pursuant
# to the terms of a commercial license agreement with RStudio, then
# this program is licensed to you under the terms of version 3 of the
# GNU Affero General Public License. This program is distributed WITHOUT
# ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
# MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
# AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
#
#

# NOTE: these should be synchronized with the enum defined in ObjectExplorerEvent.java
.rs.setVar("explorer.types", list(
   NEW        = "new",
   OPEN_NODE  = "open_node",
   CLOSE_NODE = "close_node"
))

.rs.setVar("explorer.inspectorRegistry", new.env(parent = emptyenv()))

.rs.addJsonRpcHandler("explorer_inspect_object", function(id)
{
   object <- .rs.CachedDataEnv[[id]]
   .rs.explorer.inspectObject(object)
})

.rs.addFunction("explorer.fireEvent", function(type, data = list())
{
   .rs.enqueClientEvent("object_explorer_event", list(
      type = .rs.scalar(type),
      data = data
   ))
})

.rs.addFunction("explorer.viewObject", function(object)
{
   # generate a handle for this object
   handle <- .rs.explorer.createHandle(object)
   
   # fire event to client
   .rs.explorer.fireEvent(.rs.explorer.types$NEW, handle)
})

.rs.addFunction("explorer.createHandle", function(object)
{
   # create a unique id
   id <- .rs.createUUID()
   
   # save in cached data environment
   .rs.CachedDataEnv[[id]] <- object
   
   # return a handle object
   list(id = id)
})

# NOTE: synchronize the structure of this object with
# the JSO defined in 'ObjectExplorerInspectionResult.java'
.rs.addFunction("explorer.createInspectionResult", function(object,
                                                            name = NULL,
                                                            depth = 0,
                                                            children = NULL)
{
   list(
      name      = I(name),
      type      = I(.rs.explorer.objectType(object)),
      class     = class(object),
      desc      = I(.rs.explorer.objectDesc(object)),
      size      = I(.rs.explorer.objectSize(object)),
      length    = I(length(object)),
      depth     = I(depth),
      recursive = I(is.recursive(object)),
      children  = children
   )
})

.rs.addFunction("explorer.isValidInspectionResult", function(result)
{
   if (!is.list(result))
      return(FALSE)
   
   expected <- .rs.explorer.createInspectionResult(NULL)
   keys <- names(expected)
   missing <- setdiff(keys, names(result))
   if (length(missing))
      return(FALSE)
   
   TRUE
})

.rs.addFunction("explorer.callCustomInspector", function(object,
                                                         name = NULL,
                                                         depth = 0)
{
   classes <- class(object)
   
   # find a custom inspector method in the registry
   method <- NULL
   for (class in classes) {
      candidate <- .rs.explorer.inspectorRegistry[[class]]
      if (is.function(candidate)) {
         method <- candidate
         break
      }
   }
   
   # bail if we failed to find anything relevant
   if (is.null(method))
      return(NULL)
   
   # give the user's inspection routine 1 second to produce
   # an inspection result (returns NULL if we were forced
   # to halt execution)
   result <- .rs.withTimeLimit(1, method(object))
   if (is.null(result))
      return(NULL)
   
   # attach name, depth to the returned object
   if (is.null(result$name))
      result$name <- name
   
   if (is.null(result$depth))
      result$depth <- depth
   
   # ensure that this is a valid inspection result
   if (!.rs.explorer.isValidInspectionResult(result))
      return(NULL)
   
   result
})

.rs.addFunction("explorer.inspectObject", function(object,
                                                   name = NULL,
                                                   depth = 0,
                                                   recursive = FALSE)
{
   # check for a custom registered inspector for this object's class
   result <- .rs.explorer.callCustomInspector(object, name, depth)
   if (!is.null(result))
      return(result)
   
   # default to internal inspectors
   if (is.list(object))
      .rs.explorer.inspectList(object, name, depth, recursive)
   else if (is.environment(object))
      .rs.explorer.inspectEnvironment(object, name, depth, recursive)
   else
      .rs.explorer.inspectDefault(object, name, depth, recursive)
})

.rs.addFunction("explorer.inspectList", function(object,
                                                 name = NULL,
                                                 depth = 0,
                                                 recursive = FALSE)
{
   children <- NULL
   if (recursive) {
      children <- .rs.enumerate(object, function(key, value) {
         .rs.explorer.inspectObject(value, key, depth + 1, recursive)
      })
   }
   
   .rs.explorer.createInspectionResult(object, name, depth, children)
})

.rs.addFunction("explorer.inspectEnvironment", function(object,
                                                        name = NULL,
                                                        depth = 0,
                                                        recursive = FALSE)
{
   children <- NULL
   if (recursive) {
      children <- .rs.enumerate(object, function(key, value) {
         .rs.explorer.inspectObject(value, key, depth + 1, recursive)
      })
      children <- children[order(names(children))]
   }
   
   .rs.explorer.createInspectionResult(object, name, depth, children)
})

.rs.addFunction("explorer.inspectDefault", function(object,
                                                    name = NULL,
                                                    depth = 0,
                                                    recursive = FALSE)
{
   .rs.explorer.createInspectionResult(object, name, depth)
})

.rs.addFunction("explorer.objectType", function(object)
{
   tryCatch(
      as.character(lobstr::prim_type(object)),
      error = function(e) typeof(mtcars)
   )
})

.rs.addFunction("explorer.objectDesc", function(object)
{
   tryCatch(
      as.character(lobstr::prim_desc(object)),
      error = function(e) {
         desc <- base::toString(object)
         if (nchar(desc) > 80)
            desc <- paste(substr(desc, 1, 80), "<...>")
         desc
      }
   )
})

.rs.addFunction("explorer.objectSize", function(object)
{
   tryCatch(
      as.character(lobstr::prim_size(object)),
      error = function(e) "<unknown>"
   )
})
