import { AiOutlineSearch } from 'react-icons/ai'
import { useState } from 'react'

const Search = ({ search, onSearching }) => {
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
      <button
        type="submit"
        className="text-center bg-blue-500 rounded-e-md py-1 px-1"
      >
        <AiOutlineSearch size={28} color="white" />
      </button>
    </form>
  )
}

export default Search
