import { useEffect } from 'react'
import { FaEdit, FaTrash } from 'react-icons/fa'
import { MdOutlineCreateNewFolder } from 'react-icons/md'
import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import {
  getCategories,
  setPageNumber,
  setSearch,
  setSort,
} from '../store/slices/categorySlice'
import Loading from '../components/Loading'
import { Pagination, Search, Sort } from '../components'
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

  if (!categories || isLoading) {
    return <Loading />
  }

  return (
    <div className="px-2 lg:px-7 pt-5">
      <Link
        to="create"
        className="inline-flex items-center bg-green-500 hover:shadow-green-500/4 mb-3 cursor-pointer gap-2 px-2 py-1 rounded-sm"
      >
        <MdOutlineCreateNewFolder size={28} color="white" />
        <span className="shadow-lg text-white text-md">Add</span>
      </Link>
      <div className="w-full p-4 bg-[#6a5fdf] rounded-md">
        <div className="flex flex-col md:flex-row gap-3 md:gap-0 justify-between items-center mb-5">
          <Search search={params.search} onSearching={onSearching} />
          <Sort options={sortOptions} onSortChange={handleSort} />
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
                          <Link className="p-[10px] bg-yellow-500 rounded hover:shadow-lg hover:shadow-yellow-500/50">
                            <FaEdit />
                          </Link>
                          <Link className="p-[10px] bg-red-500 rounded hover:shadow-lg hover:shadow-red-500/50">
                            <FaTrash />
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="w-full flex justify-end mt-8 bottom-4 right-4">
              <Pagination
                pageNumber={params.pageNum}
                onPageChange={handlePageNum}
                totalPages={totalPages}
              />
            </div>
          </>
        )}
      </div>

      {/* <div
          className={`w-[320px] lg:w-5/12 translate-x-100 lg:relative lg:right-0 fixed ${
            show ? 'right-0' : '-right-[340px]'
          } z-[9999] top-0 transition-all duration-500 `}
        >
          <div className="w-full pl-5">
            <div className="bg-[#6a5fdf] h-screen lg:h-auto px-3 py-2 lg:rounded-md text-[#d0d2d6]">
              <div className="flex justify-between items-center mb-4">
                <h1 className="text-[#d0d2d6] font-semibold text-xl mb-4 w-full text-center ">
                  Add Category
                </h1>

                <div onClick={() => setShow(false)} className="block lg:hidden">
                  <IoMdCloseCircle />
                </div>
              </div>

              <form
              // onSubmit={add_category}
              >
                <div className="flex flex-col w-full gap-1 mb-3">
                  <label htmlFor="name"> Category Name</label>
                  <input
                    value={state.name}
                    onChange={(e) =>
                      setState({ ...state, name: e.target.value })
                    }
                    className="px-4 py-2 focus:border-indigo-500 outline-none bg-[#ffffff] border border-slate-700 rounded-md text-[#000000]"
                    type="text"
                    id="name"
                    name="category_name"
                    placeholder="Category Name"
                  />
                </div>

                <div>
                  <label
                    className="flex justify-center items-center flex-col h-[238px] cursor-pointer border border-dashed hover:border-red-500 w-full border-[#d0d2d6]"
                    htmlFor="image"
                  >
                    {imageShow ? (
                      <img className="w-full h-full" src={imageShow} />
                    ) : (
                      <>
                        <span>
                          <FaImage />{' '}
                        </span>
                        <span>Select Image</span>
                      </>
                    )}
                  </label>
                  <input
                    // onChange={imageHandle}
                    className="hidden"
                    type="file"
                    name="image"
                    id="image"
                  />
                  <div className="mt-4">
                    <button
                      disabled={loader ? true : false}
                      className="bg-red-500 w-full hover:shadow-red-300/50 hover:shadow-lg text-white rounded-md px-7 py-2 mb-3"
                    >
                      {loader ? (
                        <PropagateLoader
                          color="#fff"
                          cssOverride={overrideStyle}
                        />
                      ) : (
                        'Add Category'
                      )}
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div> */}
    </div>
  )
}
export default CategoryPage
