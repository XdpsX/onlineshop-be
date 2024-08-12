import { useEffect } from 'react'
import { FaEdit, FaTrash } from 'react-icons/fa'
import { useDispatch, useSelector } from 'react-redux'
import {
  getCategories,
  setDelCategory,
  setPageNumber,
  setSearch,
  setShowModal,
  setSort,
  setUpdCategory,
} from '../store/slices/categorySlice'
import Loading from '../components/Loading'
import {
  AddCategory,
  DeleteCategory,
  Pagination,
  Search,
  Sort,
} from '../components'
import { sortOptions } from '../utils/data'

const CategoryPage = () => {
  const dispatch = useDispatch()
  const { categories, isLoading, params, totalPages } = useSelector(
    (state) => state.category
  )

  useEffect(() => {
    dispatch(getCategories(params))
  }, [params, dispatch])

  const onSearching = (keyword) => {
    dispatch(setSearch(keyword))
  }

  const handlePageNum = (pageNum) => {
    dispatch(setPageNumber(pageNum))
  }

  const handleSort = (sort) => {
    dispatch(setSort(sort))
  }

  const onDeleteCat = (category) => {
    dispatch(setDelCategory(category))
  }

  const onUpdateCat = (category) => {
    dispatch(setUpdCategory(category))
    dispatch(setShowModal(true))
  }

  if (!categories || isLoading) {
    return <Loading />
  }

  return (
    <>
      <div className="px-2 lg:px-7 pt-5">
        <AddCategory />
        <div className="w-full p-4 bg-[#6a5fdf] rounded-md">
          <div className="flex flex-col md:flex-row gap-3 md:gap-0 justify-between items-center mb-5">
            <Search search={params.search} onSearching={onSearching} />
            <Sort
              curSelect={params.sort}
              options={sortOptions}
              onSortChange={handleSort}
            />
          </div>
          {categories.length === 0 ? (
            <div className="text-center min-h-[340px] mt-28">
              <h2 className="text-white font-medium text-lg">
                No Category Found
              </h2>
            </div>
          ) : (
            <>
              <div className="relative overflow-x-auto">
                <table className="w-full text-lg text-left text-[#d0d2d6]">
                  <thead className="text-sm text-[#d0d2d6] uppercase border-b border-slate-700 mb-2">
                    <tr>
                      <th scope="col" className="py-3 px-4">
                        No
                      </th>
                      <th scope="col" className="py-3 px-4">
                        Name
                      </th>
                      <th scope="col" className="py-3 px-4">
                        Action
                      </th>
                    </tr>
                  </thead>

                  <tbody>
                    {categories.map((cat, i) => (
                      <tr key={cat.id}>
                        <td
                          scope="row"
                          className="py-1 px-4 font-medium whitespace-nowrap"
                        >
                          {(params.pageNum - 1) * params.pageSize + i + 1}
                        </td>
                        <td
                          scope="row"
                          className="py-1 px-4 font-medium whitespace-nowrap"
                        >
                          {cat.name}
                        </td>

                        <td
                          scope="row"
                          className="py-1 px-4 font-medium whitespace-nowrap"
                        >
                          <div className="flex justify-start items-center gap-4">
                            <button
                              onClick={() => onUpdateCat(cat)}
                              className="p-[10px] bg-yellow-500 rounded hover:shadow-lg hover:shadow-yellow-500/50"
                            >
                              <FaEdit />
                            </button>
                            <button
                              onClick={() => onDeleteCat(cat)}
                              className="p-[10px] bg-red-500 rounded hover:shadow-lg hover:shadow-red-500/50"
                            >
                              <FaTrash />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              {totalPages > 1 && (
                <div className="w-full flex justify-end mt-8 bottom-4 right-4">
                  <Pagination
                    pageNumber={params.pageNum}
                    onPageChange={handlePageNum}
                    totalPages={totalPages}
                  />
                </div>
              )}
            </>
          )}
        </div>
        <DeleteCategory />
      </div>
    </>
  )
}
export default CategoryPage
