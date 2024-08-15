import { useEffect, useState } from 'react'
import PropTypes from 'prop-types'

const Pagination = ({ pageNumber, totalPages, onPageChange }) => {
  const [isSmallScreen, setIsSmallScreen] = useState(false)

  useEffect(() => {
    const handleResize = () => {
      setIsSmallScreen(window.innerWidth < 640) // 640px là kích thước breakpoint cho sm
    }

    handleResize() // Kiểm tra kích thước khi component mount
    window.addEventListener('resize', handleResize) // Thêm event listener

    return () => {
      window.removeEventListener('resize', handleResize) // Cleanup
    }
  }, [])

  const handleFirstPage = () => {
    onPageChange(1)
  }

  const handleLastPage = () => {
    onPageChange(totalPages)
  }

  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      onPageChange(page)
    }
  }

  const renderPageNumbers = () => {
    const pageNumbers = []
    let startPage = Math.max(pageNumber - 1, 1)
    let endPage = Math.min(pageNumber + 1, totalPages)

    if (isSmallScreen) {
      // Nếu là màn hình nhỏ, chỉ hiển thị 3 nút
      startPage = Math.max(pageNumber - 1, 1)
      endPage = Math.min(pageNumber + 1, totalPages)
    } else {
      // Nếu không, hiển thị 5 nút
      startPage = Math.max(pageNumber - 2, 1)
      endPage = Math.min(pageNumber + 2, totalPages)
    }

    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(
        <button
          key={i}
          onClick={() => handlePageChange(i)}
          disabled={pageNumber === i}
          className={`text-sm px-4 py-2 border rounded ${
            pageNumber === i
              ? 'bg-blue-500 text-white'
              : 'bg-white text-blue-500'
          }`}
        >
          {i}
        </button>
      )
    }
    return pageNumbers
  }

  return (
    <div className="mx-auto flex justify-center items-center space-x-2 mt-4">
      <button
        onClick={handleFirstPage}
        disabled={pageNumber === 1}
        className="text-sm px-4 py-2 border rounded bg-white text-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        First
      </button>
      {renderPageNumbers()}
      <button
        onClick={handleLastPage}
        disabled={pageNumber === totalPages}
        className="text-sm px-4 py-2 border rounded bg-white text-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        Last
      </button>
    </div>
  )
}

Pagination.propTypes = {
  pageNumber: PropTypes.number,
  totalPages: PropTypes.number,
  onPageChange: PropTypes.func,
}

export default Pagination
