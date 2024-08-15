import { AiOutlineSearch } from 'react-icons/ai'
import { GrClear } from 'react-icons/gr'
import { useState } from 'react'
import PropTypes from 'prop-types'

const Search = ({ search, onSearching, onClear }) => {
  const [searchValue, setSearchValue] = useState(search)

  const handleSearch = () => {
    onSearching(searchValue)
  }

  return (
    <form
      onSubmit={handleSearch}
      className="inline-flex justify-between items-center bg-white rounded-md"
    >
      <input
        onChange={(e) => setSearchValue(e.target.value)}
        value={searchValue}
        className="px-2 focus:border-indigo-500 outline-none rounded-md text-black"
        type="text"
        placeholder="Search"
      />
      <button type="submit" className="text-center bg-blue-500 py-1 px-1">
        <AiOutlineSearch size={28} color="white" title="Search" />
      </button>
      <button
        onClick={onClear}
        className="text-center bg-white rounded-e-md py-1 px-1"
      >
        <GrClear size={28} color="gray" title="Clear" />
      </button>
    </form>
  )
}

Search.propTypes = {
  search: PropTypes.string,
  onSearching: PropTypes.func,
  onClear: PropTypes.func,
}

export default Search
