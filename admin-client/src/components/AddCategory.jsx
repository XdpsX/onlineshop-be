import { useState } from 'react'
import Modal from './Modal'
import { MdOutlineCreateNewFolder } from 'react-icons/md'
import { IoClose } from 'react-icons/io5'
import { FaPlus } from 'react-icons/fa'
import * as Yup from 'yup'
import { Field, Form, Formik } from 'formik'
import { useDispatch, useSelector } from 'react-redux'
import {
  addCategory,
  getCategories,
  setShowModal,
  setUpdCategory,
  updateCategory,
} from '../store/slices/categorySlice'
import { toast } from 'react-toastify'

const CategorySchema = Yup.object().shape({
  name: Yup.string()
    .max(128, 'Maximum 128 characters')
    .required('Name is required!'),
})

const AddCategory = () => {
  const dispatch = useDispatch()
  // const [showModal, setShowModal] = useState(false)
  const [error, setError] = useState(null)
  const [isProcessing, setIsProcessing] = useState(false)
  const { showModal, params, updCategory } = useSelector(
    (state) => state.category
  )
  const isUpdating = updCategory != null

  const initialValues = isUpdating
    ? updCategory
    : {
        name: '',
      }

  const closeModal = () => {
    dispatch(setShowModal(false))
    dispatch(setUpdCategory(null))
    setError(null)
  }

  const openModal = () => {
    dispatch(setShowModal(true))
  }

  const submitHandler = (values, { setFieldError, resetForm }) => {
    setIsProcessing(true)
    const processing = isUpdating ? updateCategory(values) : addCategory(values)
    dispatch(processing)
      .unwrap()
      .then(() => {
        if (isUpdating) {
          // dispatch(setUpdCategory(values))
          closeModal()
          toast.success('Update Category Successfully')
        } else {
          resetForm()
          toast.success('Add Category Successfully')
        }

        dispatch(getCategories(params))
      })
      .catch((err) => {
        setError(err.message)
        if (err.details) {
          Object.keys(err.details).forEach((field) => {
            setFieldError(field, err.details[field])
          })
        }
      })
      .finally(() => {
        setIsProcessing(false)
      })
  }

  return (
    <>
      <button
        className="inline-flex items-center bg-green-500 hover:shadow-green-500/4 mb-3 cursor-pointer gap-2 px-2 py-1 rounded-sm"
        type="button"
        onClick={openModal}
      >
        <MdOutlineCreateNewFolder size={28} color="white" />
        <span className="shadow-lg text-white text-md">Add</span>
      </button>
      <Modal isOpen={showModal} onClose={closeModal}>
        <div className="flex items-center justify-between py-2 md:p-5 border-b rounded-t dark:border-gray-600">
          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
            {isUpdating ? 'Update Category' : 'Create New Category'}
          </h3>
          <button
            type="button"
            className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-2xl ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white"
            onClick={closeModal}
          >
            <IoClose />
          </button>
        </div>
        {/* <!-- Modal body --> */}
        <Formik
          initialValues={initialValues}
          validationSchema={CategorySchema}
          onSubmit={submitHandler}
          className="p-4 md:p-5"
        >
          {({ errors, touched }) => (
            <Form>
              {error && (
                <p className="text-md font-bold text-red-600 text-center mb-2">
                  {error}
                </p>
              )}
              <div className="gap-4 mb-6">
                <label
                  htmlFor="name"
                  className="block mb-2 text-md font-medium text-gray-900 dark:text-white"
                >
                  Name
                </label>
                <Field
                  type="text"
                  name="name"
                  id="name"
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                  placeholder="Type category name"
                  required
                />
                {errors.name && touched.name && (
                  <p className="text-sm text-red-600">{errors.name}</p>
                )}
              </div>

              <button
                type="submit"
                className="text-white gap-2 inline-flex items-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                disabled={isProcessing}
              >
                <FaPlus />
                {isUpdating ? 'Update' : 'Add'}
              </button>
            </Form>
          )}
        </Formik>
      </Modal>
    </>
  )
}
export default AddCategory
