import Modal from '../Modal'
import { FaExclamationTriangle } from 'react-icons/fa'
import { useDispatch, useSelector } from 'react-redux'
import { toast } from 'react-toastify'
import {
  deleteCategory,
  getCategories,
  setDelCategory,
} from '../../store/slices/categorySlice'
import { useState } from 'react'

const DeleteCategory = () => {
  const dispatch = useDispatch()
  const { delCategory, params } = useSelector((state) => state.category)
  const [isProcessing, setIsProcessing] = useState(false)
  const showModal = delCategory !== null
  const closeModal = () => {
    dispatch(setDelCategory(null))
  }

  const deleteHandler = () => {
    setIsProcessing(true)
    dispatch(deleteCategory(delCategory.id))
      .unwrap()
      .then(() => {
        toast.success('Delete Category Successfully')
        dispatch(getCategories(params))
      })
      .catch((err) => {
        toast.error(err.message)
      })
      .finally(() => {
        closeModal()
        setIsProcessing(false)
      })
  }

  if (!delCategory) return null

  return (
    <Modal isOpen={showModal} onClose={closeModal}>
      <div className="p-4 md:p-5 text-center">
        <FaExclamationTriangle className="mx-auto mb-4 text-red-600 w-12 h-12 dark:text-gray-200" />
        <h3 className="mb-5 text-lg font-normal text-gray-700 dark:text-gray-500">
          Are you sure you want to delete category{' '}
          <strong>{delCategory.name}</strong>?
        </h3>
        {isProcessing ? (
          <button className="text-white bg-gray-600 focus:ring-4 focus:outline-none font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center cursor-none">
            Loading...
          </button>
        ) : (
          <button
            onClick={deleteHandler}
            type="button"
            className="text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center"
          >
            Yes, I&apos;m sure
          </button>
        )}
        <button
          onClick={deleteHandler}
          type="button"
          className="text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center"
        >
          Yes, I&apos;m sure
        </button>
        <button
          onClick={closeModal}
          type="button"
          className="py-2.5 px-5 ms-3 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700"
        >
          No, cancel
        </button>
      </div>
    </Modal>
  )
}
export default DeleteCategory
