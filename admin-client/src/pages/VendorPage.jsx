import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { FaEdit, FaTrash } from 'react-icons/fa'
import {
  clearFilters,
  getVendors,
  setDelVendor,
  setPageNumber,
  setSearch,
  setShowModal,
  setSort,
  setUpdVendor,
} from '../store/slices/vendorSlice'
import {
  AddVendor,
  DeleteVendor,
  Loading,
  Pagination,
  Search,
  Sort,
} from '../components'
import { sortOptions } from '../utils/data'
import defaultLogo from '../assets/images/default-vendor.png'

const VendorPage = () => {
  const dispatch = useDispatch()
  const { vendors, isLoading, params, totalPages } = useSelector(
    (state) => state.vendor
  )

  useEffect(() => {
    dispatch(getVendors(params))
  }, [params, dispatch])

  const onSearching = (keyword) => {
    dispatch(setSearch(keyword))
  }
  const onSortChange = (sort) => {
    dispatch(setSort(sort))
  }
  const onPageChange = (pageNum) => {
    dispatch(setPageNumber(pageNum))
  }

  const onDeleteVen = (vendor) => {
    dispatch(setDelVendor(vendor))
  }

  const onUpdateVen = (vendor) => {
    dispatch(setUpdVendor(vendor))
    dispatch(setShowModal(true))
  }

  if (isLoading) {
    return <Loading />
  }

  return (
    <div className="px-2 lg:px-7 pt-5">
      <AddVendor />
      <div className="w-full p-4 bg-[#6a5fdf] rounded-md">
        <div className="flex flex-col md:flex-row gap-3 md:gap-0 justify-between items-center mb-5">
          <Search
            search={params.search}
            onSearching={onSearching}
            onClear={() => dispatch(clearFilters)}
          />
          <Sort
            curSelect={params.sort}
            options={sortOptions}
            onSortChange={onSortChange}
          />
        </div>
        {vendors.length === 0 ? (
          <div className="text-center min-h-[340px] mt-28">
            <h2 className="text-white font-medium text-lg">No Vendor Found</h2>
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
                      Logo
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
                  {vendors.map((ven, i) => (
                    <tr key={ven.id}>
                      <td
                        scope="row"
                        className="py-1 px-4 font-medium whitespace-nowrap"
                      >
                        {(params.pageNum - 1) * params.pageSize + i + 1}
                      </td>
                      <td scope="row" className="py-1 px-4">
                        {ven.logo ? (
                          <img src={ven.logo} className="w-12" />
                        ) : (
                          <img src={defaultLogo} className="w-12" />
                        )}
                        {/* <img src={defaultLogo} className="w-12" /> */}
                      </td>
                      <td
                        scope="row"
                        className="py-1 px-4 font-medium whitespace-nowrap"
                      >
                        {ven.name}
                      </td>

                      <td
                        scope="row"
                        className="py-1 px-4 font-medium whitespace-nowrap"
                      >
                        <div className="flex justify-start items-center gap-4">
                          <button
                            onClick={() => onUpdateVen(ven)}
                            title="Edit"
                            className="p-[10px] bg-yellow-500 rounded hover:shadow-lg hover:shadow-yellow-500/50"
                          >
                            <FaEdit />
                          </button>
                          <button
                            onClick={() => onDeleteVen(ven)}
                            title="Delete"
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
                  onPageChange={onPageChange}
                  totalPages={totalPages}
                />
              </div>
            )}
          </>
        )}
      </div>
      <DeleteVendor />
    </div>
  )
}
export default VendorPage
