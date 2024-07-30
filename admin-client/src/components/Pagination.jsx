const Pagination = ({ pageNumber, totalPages, onPageChange }) => {
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
    let startPage = Math.max(pageNumber - 2, 1)
    let endPage = Math.min(pageNumber + 2, totalPages)

    if (pageNumber <= 3) {
      endPage = Math.min(5, totalPages)
    } else if (pageNumber >= totalPages - 2) {
      startPage = Math.max(totalPages - 4, 1)
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

export default Pagination
