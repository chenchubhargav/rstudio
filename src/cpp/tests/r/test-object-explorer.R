env <- function(...) {
   list2env(list(...))
}

l <- list(
   a = 1,
   b = list(
      b1 = "b1",
      b2 = "b2"
   ),
   c = env(
      c1 = "c1",
      c2 = "c2"
   )
)

i <- .rs.explorer.inspectObject(l, recursive = TRUE)
.rs.explorer.fireEvent("new", l)
